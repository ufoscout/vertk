package io.vertx.ext.coroutine

import io.vertx.core.Vertx
import io.vertx.core.http.HttpClientResponse
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.RunTestOnContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(VertxUnitRunner::class)
class HelloWorldTest {

    @Rule
    @JvmField val rule = RunTestOnContext()
    private lateinit var vertx: Vertx

    @Before
    fun setUp(context: TestContext) {
        vertx = rule.vertx()
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
                context.assertTrue(body.toString() == "Hello, World!")
                atc.complete()
            }
        }
    }

    @Test
    fun testSync(context: TestContext) {
        val atc = context.async()
        runVertxCoroutine {

            val response = asyncEvent<HttpClientResponse> {
                vertx.createHttpClient().getNow(8080, "localhost", "/", it)
            }.await()

            response.handler { body ->
                context.assertTrue(body.toString() == "Hello, World!")
                atc.complete()
            }
        }
    }

}