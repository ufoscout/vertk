package com.ufoscout.vertxk

import io.vertx.core.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.shareddata.SharedData
import io.vertx.core.spi.VerticleFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


class VertxkKodein {

    companion object {
        val PREFIX = "vertxk-kodein"

        /**
         * Return a [Kodein.Module] with basic [Vertx] object bound as singleton.
         */
        fun module(vertx: Vertx) = Kodein.Module {
            bind<Vertx>() with singleton { vertx }
            bind<EventBus>() with singleton { vertx.eventBus() }
            bind<FileSystem>() with singleton { vertx.fileSystem() }
            bind<SharedData>() with singleton { vertx.sharedData() }
        }

        /**
         * Register a [Kodein] aware [VerticleFactory] in a [Vertx] instance.
         * This factory handles Verticles deployed
         * with the [PREFIX] prefix and allows dependency injection of Kodein managed services.
         */
        fun registerFactory(vertx: Vertx, kodein: Kodein) {
            vertx.registerVerticleFactory(VertxkKodeinVerticleFactory(kodein))
        }
        /**
         * Deploy a [Verticle] in a [Vertx] instance using the default deployment options
         * and [VertxkKodeinVerticleFactory] factory.
         *
         * @param vertx The [Vertx] instance where the [Verticle] is deployed.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx) {
            deployVerticle<T>(vertx, DeploymentOptions())
        }

        /**
         * Deploy a [Verticle] in a [Vertx] instance
         * and [VertxkKodeinVerticleFactory] factory.
         *
         * @param vertx The [Vertx] instance where the [Verticle] is deployed.
         * @param options  the deployment options.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions) {
            vertx.deployVerticle(getFullVerticleName<T>(), options)
        }


        /**
         * Deploy a [Verticle] in a [Vertx] instance using the default deployment options
         * and [VertxkKodeinVerticleFactory] factory.
         *
         * @param vertx The [Vertx] instance where the [Verticle] is deployed.
         * @param completionHandler  a handler which will be notified when the deployment is complete.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, completionHandler: Handler<AsyncResult<String>>) {
            vertx.deployVerticle(getFullVerticleName<T>(), completionHandler)
        }

        /**
         * Deploy a [Verticle] in a [Vertx] instance using
         * and [VertxkKodeinVerticleFactory] factory.
         *
         * @param vertx The [Vertx] instance where the [Verticle] is deployed.
         * @param options  the deployment options.
         * @param completionHandler  a handler which will be notified when the deployment is complete.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions, completionHandler: Handler<AsyncResult<String>>) {
            vertx.deployVerticle(getFullVerticleName<T>(), options, completionHandler)
        }

        /**
         * Gets the name of the verticle with adding prefix required to notify vertx to use
         * [VertxkKodeinVerticleFactory] factory for verticle creation.
         */
        inline fun <reified T : Verticle> getFullVerticleName(): String {
            return PREFIX + ":" + T::class.java.canonicalName
        }

    }

}