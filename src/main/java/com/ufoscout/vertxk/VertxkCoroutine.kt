package com.ufoscout.vertxk

import io.vertx.core.*
import kotlinx.coroutines.experimental.*
import java.util.concurrent.Executor
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.suspendCoroutine

private const val VERTX_COROUTINE_DISPATCHER = "__vertx-kotlin-coroutine:dispatcher"

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

inline suspend fun <T> awaitFirst(crossinline callback: (Handler<T>) -> Unit) =
        suspendCoroutine<T> { cont ->
            callback(Handler { result: T ->
                    cont.resume(result)
            })
        }

suspend fun <T> await(callback: (Handler<T>) -> Unit) =
        suspendCoroutine<T> { cont ->
            callback(Handler { result: T ->
                cont.resume(result)
            })
        }

/**
 * Convert a standard handler to a handler which runs on a coroutine.
 * This is necessary if you want to do fiber blocking synchronous operation in your handler
 */
fun runVertxCoroutine(vertx: Vertx, block: suspend CoroutineScope.() -> Unit) {
    launch(vertx.asCoroutineDispatcher()) {
        try {
            block()
        } catch (e: CancellationException) {
            //skip this exception for coroutine cancel
        }
    }
}

/**
 * Get Kotlin CoroutineContext, this coroutine should be one instance for per context.
 * @return CoroutineContext
 */
fun vertxCoroutineContext(): CoroutineContext {
    val vertxContext = Vertx.currentContext()
    require(vertxContext.isEventLoopContext, { "Not on the vertx eventLoop." })
    var vertxContextDispatcher = vertxContext.get<CoroutineContext>(VERTX_COROUTINE_DISPATCHER)
    if (vertxContextDispatcher == null) {
        vertxContextDispatcher = VertxContextDispatcher(vertxContext, Thread.currentThread())
        vertxContext.put(VERTX_COROUTINE_DISPATCHER, vertxContextDispatcher)
    }
    return vertxContextDispatcher
}

private class VertxContextDispatcher(val vertxContext: Context, val eventLoop: Thread) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        if (Thread.currentThread() !== eventLoop) vertxContext.runOnContext { _ -> block.run() }
        else block.run()
    }
}

/**
 * Remove the scheduler for the current context
 */
fun removeVertxCoroutineContext() {
    val vertxContext = Vertx.currentContext()
    vertxContext?.remove(VERTX_COROUTINE_DISPATCHER)
}

fun Vertx.asCoroutineDispatcher() = VertxExecutor(this).asCoroutineDispatcher()

class VertxExecutor(val vertx: Vertx) : Executor {
    override fun execute(command: Runnable) = vertx.runOnContext { command.run() }
}