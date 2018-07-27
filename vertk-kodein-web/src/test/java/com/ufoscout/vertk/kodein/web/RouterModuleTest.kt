package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodein
import com.ufoscout.vertk.kodein.json.JsonModule
import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.web.client.awaitSend
import com.ufoscout.vertk.web.client.awaitSendJson
import com.ufoscout.vertk.web.client.bodyAsJson
import io.vertx.core.http.HttpServerOptions
import io.vertx.ext.web.client.WebClient
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.URL
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.stream.Collectors

class RouterModuleTest: BaseTest() {

    val port = getFreePort()
    var client = WebClient.create(vertk)

    @BeforeEach
    fun setup() = runBlocking {
        val kodein = VertkKodein.start(vertk,
                JsonModule(),
                RouterModule(RouterConfig(port), HttpServerOptions()),
                RouterTestModule()
        )
        assertNotNull(kodein.direct.instance<RouterService>())
        assertNotNull(kodein.direct.instance<WebExceptionService>())
    }

    @Test
    fun shouldThrow500() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()

        val response = client.get(port, "localhost", "/core/test/fatal/${message}").
                awaitSend()

        assertEquals(500, response.statusCode())

        val errorDetails = response.bodyAsJson<ErrorDetails>()
        assertEquals(response.statusCode(), errorDetails.code)

        assertTrue(errorDetails.message.contains("Error code:"))
        assertFalse(errorDetails.message.contains(message))
    }

    @Test
    fun badRequestExceptionShouldThrow400() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()

        val response = client.get(port, "localhost", "/core/test/badRequestException/${message}").awaitSend()

        assertEquals(400, response.statusCode())

        val errorDetails = response.bodyAsJson<ErrorDetails>()
        assertEquals(response.statusCode(), errorDetails.code)

        assertEquals(message, errorDetails.message)
    }

    @Test
    fun shouldThrowWebException() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()
        val statusCode = 400 + Random().nextInt(50)

        val response = client.get(port, "localhost", "/core/test/webException/${statusCode}/${message}").awaitSend()

        assertEquals(statusCode, response.statusCode())
        val errorDetails = response.bodyAsJson<ErrorDetails>()

        assertEquals(response.statusCode(), errorDetails.code)

        assertFalse(errorDetails.message.isEmpty())
        assertEquals(message, errorDetails.message)
    }

    @Test
    fun shouldMapWebExceptionFromCustomException() = runBlocking<Unit> {

        val response = client.get(port, "localhost", "/core/test/customException").awaitSend()
        assertEquals(12345, response.statusCode())

        val errorDetails = response.bodyAsJson<ErrorDetails>()
        assertEquals(response.statusCode(), errorDetails.code)

        assertFalse(errorDetails.message.isEmpty())
        assertEquals("CustomTestExceptionMessage", errorDetails.message)
    }

    @Test
    fun shouldThrow422andTheValidationDetails() = runBlocking<Unit> {

        val bean = BeanToValidate(null, null)

        val response = client.post(port, "localhost", "/core/test/validationException").awaitSendJson(bean)
        assertEquals(422, response.statusCode())

        val errorDetails = response.bodyAsJson<ErrorDetails>()
        assertEquals(response.statusCode(), errorDetails.code)

        //assertEquals("Input validation failed", errorDetails.message)
        assertFalse(errorDetails.details.isEmpty())
        assertEquals(2, errorDetails.details.size)
        assertEquals("id should not be null", errorDetails.details["id"]!![0])
        assertEquals("name should not be null", errorDetails.details["name"]!![0])
    }


    @Test
    fun shouldUseMultipleThreads() = runBlocking<Unit> {

        val messages = 100
        val count = CountDownLatch(messages)

        for (i in 0..messages) {
                    Thread({
                        val urlString = "http://127.0.0.1:${port}/core/test/slow"
                        val url = URL(urlString)
                        val conn = url.openConnection()
                        val stream = conn.getInputStream()
                        read(stream)
                        stream.close()
                        count.countDown()
                    }).start()
                }
        count.await()
    }

    fun read(input: InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        return reader.lines().collect(Collectors.joining("\n"))
    }

    @Synchronized private fun getFreePort(): Int {
        try {
            ServerSocket(0).use { socket ->
                socket.reuseAddress = true
                return socket.localPort
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }
}