package com.ufoscout.vertk

import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class VertxTest: BaseTest() {

    @Test
    fun shouldLaunchSuspendableFunctionsInCurrentContext(): Unit {

        val countDownLatch = CountDownLatch(1)
        val thread = AtomicReference<String>("")

        vertx.launch {
                thread.set(getThreadName())
                countDownLatch.countDown()
        }

        countDownLatch.await()
        println("Thread is: " + thread.get())
        assertTrue(thread.get().contains("vert"))
        assertFalse(thread.get().contains("worker"))
    }

    @Test
    fun shouldRunTheSuspendableFunctionsInCurrentContextBlockingTheTrhead(): Unit {

        val thread = vertx.runBlocking {
            getThreadName()
        }

        assertNotNull(thread)
        assertTrue(thread.contains("vert"))
        assertFalse(thread.contains("worker"))
    }


    suspend private fun getThreadName(): String {
        return Thread.currentThread().name
    }

    @Test
    fun shouldExecuteBlocking() = runBlocking<Unit> {

        val executed = AtomicBoolean(false)

        vertx.awaitExecuteBlocking({
            executed.set(true)
            Thread.sleep(50)
        })

        assertTrue(executed.get());

    }

    @Test
    fun shouldExecuteBlockingAndReturnTheResult() = runBlocking<Unit> {

        val uuid = UUID.randomUUID().toString()
        val executed = AtomicBoolean(false)

        val result = vertx.awaitExecuteBlocking({
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
            vertx.awaitExecuteBlocking({
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