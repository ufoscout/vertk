package com.ufoscout.vertxk


import com.ufoscout.vertxk.util.VertxkKodein
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import org.kodein.di.Kodein

abstract class VertxkModule {

    /**
     * Return a new [Kodein.Module] instance to be imported before
     * the [VertxkModule] is initialized.
     */
    open fun module() = Kodein.Module {}

    /**
     * Initialization entry point for the the [VertxkModule].
     * All [Verticle]s that belong to the module should be deployed here.
     */
    abstract suspend fun onInit(vertx: Vertx, kodein: Kodein)

    /**
     * Deploy a [Verticle] in a [Vertx] instance using the default deployment options.
     *
     * @param vertx The [Vertx] instance where the [Verticle] is deployed.
     */
    inline suspend fun <reified T : Verticle> deployVerticle(vertx: Vertx) {
        awaitResult<String> {
            VertxkKodein.deployVerticle<T>(vertx, it)
        }
    }

    /**
     * Deploy a [Verticle] in a [Vertx] instance.
     *
     * @param vertx The [Vertx] instance where the [Verticle] is deployed.
     * @param options  the deployment options.
     */
    inline suspend fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions) {
        awaitResult<String> {
            VertxkKodein.deployVerticle<T>(vertx, options, it)
        }
    }
}