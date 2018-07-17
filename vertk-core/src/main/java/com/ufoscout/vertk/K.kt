package com.ufoscout.vertk

import io.vertx.core.*
import io.vertx.kotlin.coroutines.awaitResult
import io.vertx.core.http.HttpServer;


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

    suspend fun <R> Vertx.awaitExecuteBlocking(action: () -> R, ordered: Boolean = true): R {
        return awaitResult<R> {
            val handler: Handler<Future<R>> = Handler {
                try {
                    it.complete(action())
                } catch (e: Throwable) {
                    it.fail(e)
                }
            }
            this.executeBlocking(handler, ordered, it)
        }

    }

