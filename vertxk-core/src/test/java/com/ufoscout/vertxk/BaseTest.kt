package com.ufoscout.vertxk

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Router
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.*
import org.junit.rules.TestName
import java.math.BigDecimal
import java.util.*

abstract class BaseTest: K {

    @Rule @JvmField
    val name = TestName()

    private var startTime: Date? = null

    companion object {
        var vertx = Vertx.vertx()
        var router = Router.router(vertx)
        var port = 0

        @BeforeClass @JvmStatic
        fun baseSetUp() = runBlocking<Unit> {
            vertx = Vertx.vertx()
            router = Router.router(Companion.vertx)
            awaitResult<HttpServer> { wait ->
                vertx.createHttpServer().requestHandler(Handler <HttpServerRequest> { router.accept(it) }).listen(0, {res ->
                    port = res.result().actualPort()
                    println("Http Port " + port)
                    wait.handle(res)
                })
            }
        }

        @AfterClass @JvmStatic
        fun baseTearDown() = runBlocking<Unit> {
            awaitResult<Void> { vertx.close(it) }
        }

    }

    @Before
    fun setUpBeforeTest() {
        startTime = Date()
        println("===================================================================")
        println("BEGIN TEST " + name.methodName)
        println("===================================================================")

    }

    @After
    fun tearDownAfterTest() {
        val time = BigDecimal(Date().time - startTime!!.time).divide(BigDecimal(1000)).toString()
        println("===================================================================")
        println("END TEST " + name.methodName)
        println("Execution time: $time seconds")
        println("===================================================================")
    }

}