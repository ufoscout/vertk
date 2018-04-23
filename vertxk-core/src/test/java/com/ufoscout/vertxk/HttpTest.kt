package com.ufoscout.vertxk

import com.ufoscout.vertxk.verticle.RequestDTO
import com.ufoscout.vertxk.verticle.ResponseDTO
import com.ufoscout.vertxk.verticle.HttpVerticle
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test
import java.util.*

class HttpTest: BaseTest() {

    companion object {
        @BeforeClass @JvmStatic
        fun deployStubVerticle() = runBlocking<Unit> {
            vertx.deployVerticle<HttpVerticle>()
        }
    }

    @Test
    fun shouldCallDelete() = runBlocking<Unit> {
        val response = vertx.createHttpClient()
                .restDelete(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("DELETE", response.body!!.message)
    }

    @Test
    fun shouldCallGet() = runBlocking<Unit> {
        val response = vertx.createHttpClient()
                .restGet(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("GET", response.body!!.message)
    }

    @Test
    fun shouldCallOptions() = runBlocking<Unit> {
        val response = vertx.createHttpClient()
                .restOptions(port, "localhost", HttpVerticle.path, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("OPTIONS", response.body!!.message)
    }

    @Test
    fun shouldCallPatch() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertx.createHttpClient()
                .restPatch(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PATCH-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPost() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertx.createHttpClient()
                .restPost(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("POST-${request.message}", response.body!!.message)
    }

    @Test
    fun shouldCallPut() = runBlocking<Unit> {
        val request = RequestDTO(UUID.randomUUID().toString())
        val response = vertx.createHttpClient()
                .restPut(port, "localhost", HttpVerticle.path, request, ResponseDTO::class)
        assertNotNull(response)
        assertNotNull(response.body)
        assertEquals(200, response.statusCode)
        assertEquals("PUT-${request.message}", response.body!!.message)
    }
}