package com.ufoscout.vertk

import io.vertx.core.*
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.core.http.HttpServer;
import io.vertx.core.shareddata.AsyncMap
import io.vertx.core.shareddata.Counter
import io.vertx.core.shareddata.Lock
import io.vertx.core.shareddata.SharedData
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.runBlocking


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

// SharedData extensions

suspend fun <K, V> SharedData.awaitGetAsyncMap(name: String): AsyncMap<K, V> {
    return awaitResult{
        this.getAsyncMap(name, it)
    }
}

suspend fun <K, V> SharedData.awaitGetClusterWideMap(name: String): AsyncMap<K, V> {
    return awaitResult{
        this.getClusterWideMap(name, it)
    }
}

suspend fun SharedData.awaitGetCounter(name: String): Counter {
    return awaitResult{
        this.getCounter(name, it)
    }
}

suspend fun SharedData.awaitGetLock(name: String): Lock {
    return awaitResult{
        this.getLock(name, it)
    }
}

suspend fun SharedData.awaitGetLockWithTimeout(name: String, timeout: Long): Lock {
    return awaitResult{
        this.getLockWithTimeout(name, timeout, it)
    }
}

// AsyncMap extensions

suspend fun <K, V> AsyncMap<K, V>.awaitClear(): Void {
    return awaitResult{
        this.clear(it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.awaitEntries(): Map<K, V> {
    return awaitResult{
        this.entries(it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.awaitGet(key: K): V? {
    return awaitResult{
        this.get(key, it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.awaitGetOrCompute(key: K, ifNotPresent: suspend (K) -> V): V {
    var value = awaitResult<V?>{
        this.get(key, it)
    }
    if (value == null) {
        value = ifNotPresent(key)
        this.awaitPut(key, value)
    }
    return value!!
}

suspend fun <K, V> AsyncMap<K, V>.awaitGetOrCompute(key: K, ttl: Long, ifNotPresent: suspend (K) -> V): V {
    var value = awaitResult<V?>{
        this.get(key, it)
    }
    if (value == null) {
        value = ifNotPresent(key)
        this.awaitPut(key, value, ttl)
    }
    return value!!
}

suspend fun <K, V> AsyncMap<K, V>.awaitKeys(): Set<K> {
    return awaitResult{
        this.keys(it)
    }
}

/**
 * Put a value in the map, asynchronously.
 *
 * @param k  the key
 * @param v  the value
 */
suspend fun <K, V> AsyncMap<K, V>.awaitPut(key: K, value: V): Void {
    return awaitResult{
        this.put(key, value, it)
    }
}


/**
 * Like {@link #put} but specifying a time to live for the entry. Entry will expire and get evicted after the
 * ttl.
 *
 * @param k  the key
 * @param v  the value
 * @param ttl  The time to live (in ms) for the entry
 */
suspend fun <K, V> AsyncMap<K, V>.awaitPut(key: K, value: V, ttl: Long): Void {
    return awaitResult{
        this.put(key, value, ttl, it)
    }
}

suspend fun <K, V> AsyncMap<K, V>.awaitRemove(key: K): V? {
    return awaitResult{
        this.remove(key, it)
    }
}

