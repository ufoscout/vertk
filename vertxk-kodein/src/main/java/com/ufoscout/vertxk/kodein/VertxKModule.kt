package com.ufoscout.vertxk.kodein
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import org.kodein.di.Kodein
import kotlin.reflect.KClass

interface VertxKModule {

    /**
     * Return a new [Kodein.Module] instance to be imported before
     * the [VertxkModule] is initialized.
     */
    fun module(): Kodein.Module

    /**
     * Initialization entry point for the the [VertxkModule].
     * All [Verticle]s that belong to the module should be deployed here.
     */
    suspend fun onInit(vertx: Vertx, kodein: Kodein)

    /**
     * Deploy a [Verticle] in a [Vertx] instance using
     * and [VertxkKodeinVerticleFactory] factory.
     *
     * @param options  the deployment options.
     */
    suspend fun <T : Verticle> Vertx.deployKodeinVerticle(kClass: KClass<T>, deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        awaitResult<String> {
            deployVerticle(kClass.javaObjectType, deploymentOptions, it)
        }
    }
}

/**
 * Deploy a [Verticle] in a [Vertx] instance using
 * and [VertxkKodeinVerticleFactory] factory.
 *
 * @param options  the deployment options.
 */
inline suspend fun <reified T : Verticle> Vertx.deployKodeinVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    awaitResult<String> {
        deployVerticle(VertxkKodeinVerticleFactory.PREFIX + ":" + T::class.java.canonicalName, deploymentOptions, it)
    }
}
