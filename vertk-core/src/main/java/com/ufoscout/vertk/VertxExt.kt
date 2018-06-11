package com.ufoscout.vertk

import io.vertx.core.*
import io.vertx.kotlin.coroutines.awaitResult
import kotlin.reflect.KClass

interface VertxExt {

    suspend fun <T : Verticle> Vertx.deployVerticle(kClass: KClass<T>, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            deployVerticle(kClass.javaObjectType, deploymentOptions, it)
        }
    }

    suspend fun <R> Vertx.executeBlocking(action: () -> R, ordered: Boolean = true): R {
        return awaitResult<R> {
            val handler: Handler<Future<R>> = Handler {
                try {
                    it.complete(action())
                } catch (e: Throwable) {
                    it.fail(e)
                }
            }
            executeBlocking(handler, ordered, it)
        }

    }

}

inline suspend fun <reified T : Verticle> Vertx.deployVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        deployVerticle(T::class.java, deploymentOptions, it)
    }
}