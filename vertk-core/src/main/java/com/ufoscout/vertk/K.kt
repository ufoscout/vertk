package com.ufoscout.vertk

import com.ufoscout.vertk.eventbus.EventBusWithGroups
import io.vertx.core.*
import io.vertx.core.http.HttpServer
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.kotlin.coroutines.dispatcher
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
 * Launch a suspendable function in the current Vertx context
 */
fun Vertx.launch(action: suspend () -> Unit) {
    kotlinx.coroutines.experimental.launch(this.orCreateContext.dispatcher()) {
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

fun <R> Vertx.executeBlocking(action: suspend () -> R, ordered: Boolean = true) {
    this.launch {
        this.awaitExecuteBlocking({
            action()
        }, ordered)
    }
}

suspend fun <R> Vertx.awaitExecuteBlocking(action: suspend () -> R, ordered: Boolean = true): R {
    return awaitResult<R> {
        val handler: Handler<Future<R>> = Handler {
            try {
                val result = runBlocking(this.orCreateContext.dispatcher()) { action() }
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
fun <T> Vertx.eventBusWithGroups(address: String, member: String = UUID.randomUUID().toString()): EventBusWithGroups<T> {
    return EventBusWithGroups(vertx = this, address = address, member = member)
}
