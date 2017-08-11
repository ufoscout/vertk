package com.ufoscout.vertxk.http

import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.await
import com.ufoscout.vertxk.awaitResult
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import kotlinx.coroutines.experimental.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class HelloWorldTest : BaseTest() {

    private val vertx = Vertx.vertx()

    @Before
    fun setUp() = runBlocking<Unit> {
        awaitResult<String> { vertx.deployVerticle(HelloWorldVerticle::class.java!!.getName(), it) }
    }


    @After
    fun tearDown() = runBlocking<Unit> {
        awaitResult<Void> { vertx.close(it) }
    }

    @Test
    fun testSync1() = runBlocking<Unit> {
        val body = await<Buffer> {
            vertx.createHttpClient().getNow(8080, "localhost", "/", { response -> response.bodyHandler(it)} )
        }
        Assert.assertTrue(body.toString().equals("Hello, World!"))
        println("Found $body")
    }

    // The assertion in this test is never called because it is called in an async block
    @Test
    fun testSync2() = runBlocking<Unit> {
        val response = await<HttpClientResponse> {
                vertx.createHttpClient().getNow(8080, "localhost", "/", it )
        }
        response.bodyHandler { body ->
            Assert.assertTrue(body.toString().equals("Hello, World!"))
            println("Found $body")
        }
    }

    // This test has a race-condition; in fact, the response bodyHandler could be called once the
    // the body received handler was already called.
    /*
    @Test
    fun testSync3() = runBlocking {
        val response = await<HttpClientResponse> {
            vertx.createHttpClient().getNow(8080, "localhost", "/", it )
        }
        val body = await<Buffer> {
            response.bodyHandler( it )
        }
        Assert.assertTrue(body.toString().equals("Hello, World!"))
        println("Found $body")
    }
    */

}