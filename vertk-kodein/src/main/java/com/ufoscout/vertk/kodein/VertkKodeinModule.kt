package com.ufoscout.vertk.kodein

import com.ufoscout.vertk.Vertk
import io.vertx.core.DeploymentOptions
import io.vertx.core.Verticle
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
    suspend fun onInit(vertk: Vertk, kodein: Kodein)

}

/**
 * Deploy a [Verticle] in a [Vertx] instance using
 * and [VertkKodeinVerticleFactory] factory.
 *
 * @param options  the deployment options.
 */
inline suspend fun <reified T : Verticle> Vertk.deployKodeinVerticle(deploymentOptions: DeploymentOptions = DeploymentOptions()) {
        deployVerticle(VertkKodeinVerticleFactory.PREFIX + ":" + T::class.java.canonicalName, deploymentOptions)
}
