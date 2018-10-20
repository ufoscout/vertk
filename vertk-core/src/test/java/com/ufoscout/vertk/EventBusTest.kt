package com.ufoscout.vertk

import com.ufoscout.vertk.eventbus.awaitConsumer
import com.ufoscout.vertk.eventbus.awaitSend
import io.vertx.core.eventbus.Message
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class EventBusTest: BaseTest() {

    @Test
    fun shouldSendAndAwait() = runBlocking<Unit> {

        val address = UUID.randomUUID().toString()
        val request = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        vertx.eventBus().consumer(address) { message: Message<String> ->
            assertEquals(request, message.body())
            message.reply(response)
        }

        val message = vertx.eventBus().awaitSend<String>(address, request)
        assertEquals(response, message.body())

    }

    @Test
    fun shouldPublishAndAwait() = runBlocking<Unit> {

        val address = UUID.randomUUID().toString()
        val request = UUID.randomUUID().toString()
        val response = UUID.randomUUID().toString()

        vertx.eventBus().awaitConsumer(address) { message: String ->
            assertEquals(request, message)
            response
        }

        val message = vertx.eventBus().awaitSend<String>(address, request)
        assertEquals(response, message.body())

    }


}