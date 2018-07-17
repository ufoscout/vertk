package com.ufoscout.vertk

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

abstract class BaseTest {

    private val TIME_FORMAT = DecimalFormat("####,###.###", DecimalFormatSymbols(Locale.US))
    protected val logger = LoggerFactory.getLogger(this.javaClass)
    private var testStartDate: Long = 0

    var vertk = Vertx.vertx()

    @BeforeEach
    fun baseSetUp() = runBlocking<Unit> {
        vertk = Vertx.vertx()
    }

    @AfterEach
    fun baseTearDown() = runBlocking<Unit> {
        vertk.awaitClose()
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