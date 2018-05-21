package com.ufoscout.vertxk.kodein.router

import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.K
import com.ufoscout.vertxk.kodein.VertxK
import com.ufoscout.vertxk.kodein.config.RouterConfig
import com.ufoscout.vertxk.kodein.json.JsonModule
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

class RouterModuleTest: BaseTest(), K {

    val port = getFreePort()

    @BeforeEach
    fun setup() = runBlocking {
        val kodein = VertxK.start(vertx,
                JsonModule(),
                RouterModule(RouterConfig(port)),
                RouterTestModule()
        )
        assertNotNull(kodein.direct.instance<RouterService>())
        assertNotNull(kodein.direct.instance<WebExceptionService>())
    }

    @Test
    fun shouldThrow500() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()

        val response = vertx.createHttpClient().restGet(port, "localhost", "/core/test/fatal/${message}", ErrorDetails::class)
        assertEquals(500, response.statusCode)

        val errorDetails = response.body!!
        assertEquals(response.statusCode, errorDetails.code)

        assertTrue(errorDetails.message.contains("Error code:"))
        assertFalse(errorDetails.message.contains(message))
    }

    @Test
    fun shouldThrowWebException() = runBlocking<Unit> {

        val message = UUID.randomUUID().toString()
        val statusCode = 400 + Random().nextInt(50)

        val response = vertx.createHttpClient().restGet(port, "localhost", "/core/test/webException/${statusCode}/${message}", ErrorDetails::class)

        assertEquals(statusCode, response.statusCode)
        val errorDetails = response.body!!

        assertEquals(response.statusCode, errorDetails.code)

        assertFalse(errorDetails.message.isEmpty())
        assertEquals(message, errorDetails.message)
    }

    @Test
    fun shouldMapWebExceptionFromCustomException() = runBlocking<Unit> {

        val response = vertx.createHttpClient().restGet(port, "localhost", "/core/test/customException", ErrorDetails::class)
        assertEquals(12345, response.statusCode)

        val errorDetails = response.body!!
        assertEquals(response.statusCode, errorDetails.code)

        assertFalse(errorDetails.message.isEmpty())
        assertEquals("CustomTestExceptionMessage", errorDetails.message)
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