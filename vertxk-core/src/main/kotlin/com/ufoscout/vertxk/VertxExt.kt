package com.ufoscout.vertxk

import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlin.reflect.KClass

interface VertxExt {

    suspend fun <T : Verticle> Vertx.deployVerticle(kClass: KClass<T>, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            deployVerticle(kClass.javaObjectType, deploymentOptions, it)
        }
    }

}

inline suspend fun <reified T : Verticle> Vertx.deployVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        deployVerticle(T::class.java, deploymentOptions, it)
    }
}