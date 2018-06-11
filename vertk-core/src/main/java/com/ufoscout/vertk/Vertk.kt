package com.ufoscout.vertk

import io.vertx.core.*
import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.http.HttpServerRequest
import io.vertx.kotlin.coroutines.awaitResult
import kotlin.reflect.KClass

class Vertk(val vertx: Vertx = Vertx.vertx()) {

    companion object {
        fun vertk(): Vertk = Vertk()
    }

    suspend fun close() {
        awaitResult<Void> { vertx.close(it) }
    }

    fun createHttpClient(httpClientOptions: HttpClientOptions = HttpClientOptions()): HttpClient {
        return HttpClient(this, httpClientOptions)
    }

    fun createHttpServer(httpServerOptions: HttpServerOptions = HttpServerOptions()): com.ufoscout.vertk.HttpServer {
        return HttpServer(vertx.createHttpServer(httpServerOptions))
    }

    suspend fun deployVerticle(name: String, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            vertx.deployVerticle(name, deploymentOptions, it)
        }
    }

    suspend fun <T : Verticle> deployVerticle(kClass: KClass<T>, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            vertx.deployVerticle(kClass.javaObjectType, deploymentOptions, it)
        }
    }

    suspend fun <R> executeBlocking(action: () -> R, ordered: Boolean = true): R {
        return awaitResult<R> {
            val handler: Handler<Future<R>> = Handler {
                try {
                    it.complete(action())
                } catch (e: Throwable) {
                    it.fail(e)
                }
            }
            vertx.executeBlocking(handler, ordered, it)
        }

    }

    fun eventBus() = vertx.eventBus()
    fun sharedData() = vertx.sharedData()
    fun fileSystem() = vertx.fileSystem()

    inline suspend fun <reified T : Verticle> deployVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            vertx.deployVerticle(T::class.java, deploymentOptions, it)
        }
    }

    fun vertx(): Vertx {
        return vertx
    }

}