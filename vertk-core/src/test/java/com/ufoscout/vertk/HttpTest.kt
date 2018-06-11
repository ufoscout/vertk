package com.ufoscout.vertk

import com.ufoscout.vertk.verticle.HttpVerticle
import com.ufoscout.vertk.verticle.RequestDTO
import com.ufoscout.vertk.verticle.ResponseDTO
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
                .restDelete(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("DELETE", response.body!!.message)
    }

    @Test
    fun shouldCallGet() = runBlocking<Unit> {
        val response = vertk.createHttpClient()
                .restGet(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("GET", response.body!!.message)
    }

    @Test
    fun shouldCallOptions() = runBlocking<Unit> {
        val response = vertk.createHttpClient()
                .restOptions(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("OPTIONS", response.body!!.message)
    }

    @Test
    fun shouldCallPatch() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPatch(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PATCH-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPost() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPost(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("POST-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPut() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertk.createHttpClient()
                .restPut(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PUT-${request.message}", response.body!!.message)
    }
}