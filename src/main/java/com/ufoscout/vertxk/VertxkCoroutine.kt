package com.ufoscout.vertxk

import io.vertx.core.*
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.suspendCoroutine

inline suspend fun <T> awaitResult(crossinline callback: (Handler<AsyncResult<T>>) -> Unit) =
        suspendCoroutine<T> { cont ->
            callback(Handler { result: AsyncResult<T> ->
                if (result.succeeded()) {
                    cont.resume(result.result())
                } else {
                    cont.resumeWithException(result.cause())
                }
            })
        }

inline suspend fun <T> await(crossinline callback: (Handler<T>) -> Unit) =
        suspendCoroutine<T> { cont ->
            callback(Handler { result: T ->
                cont.resume(result)
            })
        }

/**
 * Awaits for completion of future without blocking eventLoop
 */
suspend fun <T> Future<T>.await(): T = when {
    succeeded() -> result()
    failed() -> throw cause()
    else -> suspendCancellableCoroutine { cont: CancellableContinuation<T> ->
        setHandler { asyncResult ->
            if (asyncResult.succeeded()) cont.resume(asyncResult.result() as T)
            else cont.resumeWithException(asyncResult.cause())
        }
    }
}

fun <T> streamAdaptor(vertx: Vertx) = HandlerReceiverAdaptorImpl<T>(vertx.asCoroutineDispatcher())

interface ReceiverAdaptor<out T> {
    /**
     * Receive a object from channel.
     */
    suspend fun receive(): T

    /**
     * Receive a object from channel with specific timeout.
     * @param timeout
     * @return object or null if timeout
     */
    suspend fun receive(timeout: Long, unit: TimeUnit = TimeUnit.MILLISECONDS): T?
}

class HandlerReceiverAdaptorImpl<T>(val coroutineContext: CoroutineContext, val channel: Channel<T> = Channel()) : Handler<T>, ReceiverAdaptor<T> {

    override fun handle(event: T) {
        launch(coroutineContext) {
            channel.send(event)
        }
    }

    override suspend fun receive(): T = channel.receive()

    override suspend fun receive(timeout: Long, unit: TimeUnit): T? {
        return withTimeout<T?>(timeout, unit) {
            try {
                channel.receive()
            } catch (e: CancellationException) {
                null
            } catch (t: Throwable) {
                throw VertxException(t)
            }
        }
    }
}

fun runVertxCoroutine(vertx: Vertx, block: suspend CoroutineScope.() -> Unit) {
    launch(vertx.asCoroutineDispatcher()) {
        try {
            block()
        } catch (e: CancellationException) {
            //skip this exception for coroutine cancel
        }
    }
}

fun Vertx.asCoroutineDispatcher() = VertxExecutor(this).asCoroutineDispatcher()

class VertxExecutor(val vertx: Vertx) : Executor {
    override fun execute(command: Runnable) = vertx.runOnContext { command.run() }
}