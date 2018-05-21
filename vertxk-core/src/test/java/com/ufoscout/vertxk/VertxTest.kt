package com.ufoscout.vertxk

import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class VertxTest: BaseTest() {

    @Test
    fun shouldExecuteBlocking() = runBlocking<Unit> {

        val executed = AtomicBoolean(false)

        vertx.executeBlocking({
            executed.set(true)
        })

        assertTrue(executed.get());

    }

    @Test
    fun shouldExecuteBlockingAndReturnTheResult() = runBlocking<Unit> {

        val uuid = UUID.randomUUID().toString()
        val executed = AtomicBoolean(false)

        val result = vertx.executeBlocking({
            executed.set(true)
            uuid
        })

        assertTrue(executed.get());
        assertEquals(uuid, result);

    }

    @Test
    fun shouldExecuteBlockingAndFailIfException() = runBlocking<Unit> {

        val uuid = UUID.randomUUID().toString()
        val executed = AtomicBoolean(false)

        try {
            vertx.executeBlocking({
                executed.set(true)
                throw RuntimeException(uuid)
            })
            fail("Should throw exception before arriving here!")
        } catch (e: RuntimeException) {
            assertEquals(uuid, e.message)
        }

        assertTrue(executed.get());

    }

}