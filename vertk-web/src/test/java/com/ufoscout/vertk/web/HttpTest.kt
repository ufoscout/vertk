package com.ufoscout.vertk.web

import com.ufoscout.vertk.web.verticle.HttpVerticle
import com.ufoscout.vertk.web.verticle.RequestDTO
import com.ufoscout.vertk.web.verticle.ResponseDTO
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

class HttpTest: BaseTest() {

    companion object {
        @BeforeAll @JvmStatic
        fun deployStubVerticle() = runBlocking<Unit> {
            vertk.deployVerticle<HttpVerticle>()
        }
    }

    @Test
    fun shouldCallDelete() = runBlocking<Unit> {
        val response = vertk.createHttpClient()
                .restDelete<ResponseDTO>(port, "localhost", HttpVerticle.path)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("DELETE", response.body!!.message)
    }

    @Test
    fun shouldCallGet() = runBlocking<Unit> {
        val response = vertk.createHttpClient()
                .restGet<ResponseDTO>(port, "localhost", HttpVerticle.path)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("GET", response.body!!.message)
    }

    @Test
    fun shouldCallOptions() = runBlocking<Unit> {
        val response = vertk.createHttpClient()
                .restOptions<ResponseDTO>(port, "localhost", HttpVerticle.path)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("OPTIONS", response.body!!.message)
    }

    @Test
    fun shouldCallPatch() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPatch<ResponseDTO>(port, "localhost", HttpVerticle.path, request)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PATCH-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPost() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPost<ResponseDTO>(port, "localhost", HttpVerticle.path, request)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("POST-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPut() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPut<ResponseDTO>(port, "localhost", HttpVerticle.path, request)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PUT-${request.message}", response.body!!.message)
    }
}