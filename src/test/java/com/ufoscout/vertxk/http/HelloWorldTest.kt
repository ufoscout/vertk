package com.ufoscout.vertxk.http

import com.ufoscout.vertxk.awaitFirst
import com.ufoscout.vertxk.awaitResult
import com.ufoscout.vertxk.runVertxCoroutine
import com.ufoscout.vertxk.vertxCoroutineContext
import io.vertx.core.*
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.runner.RunWith
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import io.vertx.ext.unit.junit.RunTestOnContext
import kotlinx.coroutines.experimental.*
import org.junit.*
import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.experimental.suspendCoroutine


@RunWith(VertxUnitRunner::class)
class HelloWorldTest {

    @Rule @JvmField val rule = RunTestOnContext()
    private lateinit var vertx: Vertx

    @Before
    fun setUp(context: TestContext)
    //        = runBlocking<Unit>
    {
        vertx = rule.vertx()
        //awaitResult<String> { vertx.deployVerticle(HelloWorldVerticle::class.java!!.getName(), it) }

        vertx.deployVerticle(HelloWorldVerticle::class.java!!.getName(), context.asyncAssertSuccess())

    }

    @After
    fun tearDown(context: TestContext)
    //        = runBlocking<Unit>
    {
      //  awaitResult<Void> { vertx.close(it) }
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testAsync(context: TestContext) {
        val atc = context.async()
        vertx.createHttpClient().getNow(8080, "localhost", "/") { response ->
            response.handler { body ->
                context.assertTrue(body.toString().equals("Hello, World!"))
                atc.complete()
            }
        }
    }

    @Test
    fun testSync1(context: TestContext) = runVertxCoroutine {
        val atc = context.async()
        val body = await<Buffer> {
            vertx.createHttpClient().getNow(8080, "localhost", "/", { response -> response.bodyHandler(it)} )
        }
        context.assertTrue(body.toString().equals("Hello, World!"))
        atc.complete()
    }

    @Test
    fun testSync2(context: TestContext) = runVertxCoroutine {
        val atc = context.async()
        val response = await<HttpClientResponse> {
                vertx.createHttpClient().getNow(8080, "localhost", "/", it )
        }
        response.handler { body ->
            context.assertTrue(body.toString().equals("Hello, World!"))
            atc.complete()
        }

    }

    @Test
    fun testSync3(context: TestContext) = runVertxCoroutine {
        val atc = context.async()
        val response = await<HttpClientResponse> {
            println(1)
            vertx.createHttpClient().getNow(8080, "localhost", "/", it )
        }

        println(2)
        val body = await<Buffer> {
            println(3)
            response.bodyHandler( it )
        }

        println(4)
        context.assertTrue(body.toString().equals("Hello, World!"))

        println(5)
        atc.complete()

        println(6)

    }

    suspend fun <T> await(callback: (Handler<T>) -> Unit) =
            suspendCoroutine<T> { cont ->
                callback(Handler { result: T ->
                    cont.resume(result)
                })
            }
}