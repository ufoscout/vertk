package com.ufoscout.vertk.eventbus

import io.vertx.core.Vertx
import io.vertx.core.eventbus.*
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch
import java.lang.Exception

suspend inline fun <T> EventBus.awaitSend(address: String, message: Any): Message<T> {
    return awaitResult<Message<T>> {
        this.send<T>(address, message, it)
    }
}

suspend inline fun <T> EventBus.awaitSend(address: String, message: Any, options: DeliveryOptions): Message<T> {
    return awaitResult<Message<T>> {
        this.send<T>(address, message, options, it)
    }
}

/**
 * To return a custom message failure code, the callback can throw a ReplyException
 */
inline fun <T> EventBus.awaitConsumer(address: String, noinline handler: suspend (message: T) -> Any): MessageConsumer<T> {
    return this.consumer(address) { message: Message<T> ->
        launch(Vertx.currentContext().dispatcher()) {
            try {
                message.reply(handler(message.body()))
            } catch (e: ReplyException) {
                message.fail(e.failureCode(), e.message)
            } catch (e: Exception) {
                message.fail(0, e.message)
            }
        }
    }
}

suspend fun EventBus.awaitClose() {
    awaitResult<Void> { this.close(it) }
}

suspend fun <T> MessageConsumer<T>.awaitUnregister() {
    awaitResult<Void> { this.unregister(it) }
}