package com.ufoscout.vertxk.http

import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.await
import com.ufoscout.vertxk.awaitResult
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test


class HelloWorldTest : BaseTest() {

    var port = 0;

    @Before
    fun setUp() = runBlocking<Unit> {
        awaitResult<String> { vertx.deployVerticle(HelloWorldVertxkVerticle::class.java!!.getName(), it) }
        port = HelloWorldVertxkVerticle.port
        Assert.assertTrue(port!=0)
    }

    @Test
    fun testSync1() = runBlocking<Unit> {
        val body = await<Buffer> {
            vertx.createHttpClient().getNow(port, "localhost", "/", { response -> response.bodyHandler(it)} )
        }
        Assert.assertTrue(body.toString().equals("Hello, World!"))
        println("Found $body")
    }

    // The assertion in this test is never called because it is called in an async block
    @Test
    fun testSync2() = runBlocking<Unit> {
        val response = await<HttpClientResponse> {
                vertx.createHttpClient().getNow(port, "localhost", "/", it )
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
            vertx.createHttpClient().getNow(port, "localhost", "/", it )
        }
        val body = await<Buffer> {
            response.bodyHandler( it )
        }
        Assert.assertTrue(body.toString().equals("Hello, World!"))
        println("Found $body")
    }
    */

}