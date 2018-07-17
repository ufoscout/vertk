package com.ufoscout.vertk.kodein

import com.ufoscout.vertk.awaitDeployVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
import io.vertx.core.Vertx
import org.kodein.di.Kodein

interface VertkKodeinModule {

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

}

/**
 * Deploy a [Verticle] in a [Vertx] instance using
 * and [VertkKodeinVerticleFactory] factory.
 *
 * @param options  the deployment options.
 */
inline suspend fun <reified T : Verticle> Vertx.awaitDeployKodeinVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
    this.awaitDeployVerticle(VertkKodeinVerticleFactory.PREFIX + ":" + T::class.java.canonicalName, deploymentOptions)
}
