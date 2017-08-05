package io.vertx.ext.coroutine

import com.ufoscout.vertxk.awaitFirst
import com.ufoscout.vertxk.awaitResult
import io.vertx.core.*
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.runner.RunWith
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClientResponse
import io.vertx.ext.unit.junit.RunTestOnContext
import kotlinx.coroutines.experimental.*
import org.junit.*
import kotlin.coroutines.experimental.suspendCoroutine


@RunWith(VertxUnitRunner::class)
class HelloWorldTest {

    @Rule @JvmField val rule = RunTestOnContext()
    private lateinit var vertx: Vertx

    @Before
    fun setUp(context: TestContext) {
        vertx = Vertx.vertx()
        vertx.deployVerticle(HelloWorldVerticle::class.java!!.getName(), context.asyncAssertSuccess())
    }

    @After
    fun tearDown(context: TestContext) {
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
    fun testSync1(context: TestContext) {
        val atc = context.async()

        attachVertxToCoroutine(vertx)

        runVertxCoroutine {
            val response = asyncEvent<HttpClientResponse> { vertx.createHttpClient().getNow(8080, "localhost", "/", it) }.await()
            response.handler { body ->
                context.assertTrue(body.toString().equals("Hello, World!"))
                atc.complete()
            }
        }
    }

    @Test
    fun testSync2(context: TestContext) {
        val atc = context.async()

        attachVertxToCoroutine(vertx)

        runVertxCoroutine {
            val response = asyncEvent<HttpClientResponse> {
                vertx.createHttpClient().getNow(8080, "localhost", "/") { it }
            }.await()

            response.handler { body ->
                context.assertTrue(body.toString().equals("Hello, World!"))
                atc.complete()
            }
        }
    }



}