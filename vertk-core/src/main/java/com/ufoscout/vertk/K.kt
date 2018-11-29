package com.ufoscout.vertk

import com.ufoscout.vertk.eventbus.Addr
import com.ufoscout.vertk.eventbus.EventBusWithGroups
import io.vertx.core.*
import io.vertx.core.http.HttpServer
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.runBlocking
import java.util.*


suspend fun HttpServer.awaitListen(port: Int): HttpServer {
    awaitResult<io.vertx.core.http.HttpServer> { wait ->
        this.listen(port, wait)
    }
    return this
}

suspend fun HttpServer.awaitListen(port: Int, host: String): HttpServer {
    awaitResult<io.vertx.core.http.HttpServer> { wait ->
        this.listen(port, host, wait)
    }
    return this
}

/**
 * Executes a suspendable function in the current Vertx context without blocking current thread.
 * This uses the kotlin coroutines "launch()" using the current Vertx dispatcher.
 */
fun Vertx.launch(action: suspend () -> Unit) {
    kotlinx.coroutines.experimental.launch(this.orCreateContext.dispatcher()) {
        action()
    }
}

/**
 * Executes a suspendable function in the current Vertx context.
 * This uses the kotlin coroutines "runBlocking()" using the current Vertx dispatcher.
 *
 * WARN: this methods BLOCKS the current thread!
 */
fun <T> Vertx.runBlocking(action: suspend () -> T): T {
    return kotlinx.coroutines.experimental.runBlocking(this.orCreateContext.dispatcher()) {
        action()
    }
}

suspend fun Vertx.awaitClose() {
    awaitResult<Void> { this.close(it) }
}

suspend fun Vertx.awaitDeployVerticle(name: String, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(name, deploymentOptions, it)
    }
}

suspend fun Vertx.awaitDeployVerticle(supplier: () -> Verticle, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(java.util.function.Supplier { supplier() }, deploymentOptions, it)
    }
}

suspend fun Vertx.awaitDeployVerticle(verticle: Verticle, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(verticle, deploymentOptions, it)
    }
}

inline suspend fun <reified T : Verticle> Vertx.awaitDeployVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        this.deployVerticle(T::class.java, deploymentOptions, it)
    }
}

suspend fun <R> Vertx.awaitExecuteBlocking(action: () -> R, ordered: Boolean = true): R {
    return awaitResult<R> {
        val handler: Handler<Future<R>> = Handler {
            try {
                val result = action()
                it.complete(result)
            } catch (e: Throwable) {
                it.fail(e)
            }
        }
        this.executeBlocking(handler, ordered, it)
    }
}

/**
 * Returns a new EventBusWithGroups
 */
fun <T> Vertx.eventBusWithGroups(address: Addr<T>): EventBusWithGroups<T> {
    return EventBusWithGroups(vertx = this, address = address)
}
