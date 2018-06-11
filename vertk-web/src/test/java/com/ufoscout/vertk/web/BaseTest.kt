package com.ufoscout.vertk.web

import com.ufoscout.vertk.Vertk
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.logging.LoggerFactory
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

abstract class BaseTest {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var testStartDate: Long = 0

    companion object {

        protected val TIME_FORMAT = DecimalFormat("####,###.###", DecimalFormatSymbols(Locale.US))
        private val TEMP_DIR = "./target/junit-temp/" + System.currentTimeMillis()

        var vertk = Vertk.vertk()
        var router = Router.router(vertk)
        var port = 0

        @BeforeAll @JvmStatic
        fun baseSetUp() = runBlocking<Unit> {
            vertk = Vertk.vertk()
            router = Router.router(vertk)
            port = vertk.createHttpServer().requestHandler(Handler <HttpServerRequest> { router.accept(it) }).listen(0).actualPort()
            println("Http Port " + port)
        }

        @AfterAll @JvmStatic
        fun baseTearDown() = runBlocking<Unit> {
            vertk.close()
        }

    }

    @BeforeEach
    fun setUpBeforeTest(testInfo: TestInfo) {
        testStartDate = System.currentTimeMillis()
        logger.info("===================================================================")
        logger.info("BEGIN TEST " + testInfo.displayName)
        logger.info("===================================================================")

    }

    @AfterEach
    fun tearDownAfterTest(testInfo: TestInfo) {
        val executionTime = System.currentTimeMillis() - testStartDate
        logger.info("===================================================================")
        logger.info("END TEST " + testInfo.displayName)
        logger.info("execution time: " + TIME_FORMAT.format(executionTime) + " ms")
        logger.info("===================================================================")
    }

}