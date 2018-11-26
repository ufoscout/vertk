package com.ufoscout.vertk.eventbus

import com.ufoscout.vertk.BaseTest
import io.vertx.core.eventbus.Message
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

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