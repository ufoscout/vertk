package com.ufoscout.vertxk

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import io.vertx.core.*
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.shareddata.SharedData
import io.vertx.core.spi.VerticleFactory


class VertxkKodein {

    companion object {
        val PREFIX = "vertxk-kodein"

        fun module(vertx: Vertx) = Kodein.Module {
            bind<Vertx>() with singleton { vertx }
            bind<EventBus>() with singleton { vertx.eventBus() }
            bind<FileSystem>() with singleton { vertx.fileSystem() }
            bind<SharedData>() with singleton { vertx.sharedData() }
        }

        fun registerFactory(vertx: Vertx, kodein: Kodein) {
            registerFactory(vertx, VertxkKodeinVerticleFactory(kodein))
        }

        fun registerFactory(vertx: Vertx, factory: VerticleFactory) {
            vertx.registerVerticleFactory(factory)
        }

        /**
         * Deploy a verticle instance given a class of the verticle using default deployment options
         * and [GuiceVerticleFactory] factory.
         *
         * @param verticleClazz the class of the verticle to deploy.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx) {
            deployVerticle<T>(vertx, DeploymentOptions())
        }

        /**
         * Like [.deployVerticle] but [io.vertx.core.DeploymentOptions] are provided to configure the
         * deployment.
         *
         * @param verticleClazz  the class of the verticle to deploy.
         * @param options  the deployment options.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions) {
            vertx.deployVerticle(getFullVerticleName<T>(), options)
        }


        /**
         * Like [.deployVerticle] but handler can be provided
         * which will be notified when the deployment is complete.
         *
         * @param verticleClazz  the class of the verticle to deploy.
         * @param completionHandler  a handler which will be notified when the deployment is complete.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, completionHandler: Handler<AsyncResult<String>>) {
            vertx.deployVerticle(getFullVerticleName<T>(), completionHandler)
        }

        /**
         * Like [.deployVerticle] but handler can be provided
         * which will be notified when the deployment is complete.
         *
         * @param verticleClazz  the class of the verticle to deploy.
         * @param options  the deployment options.
         * @param completionHandler  a handler which will be notified when the deployment is complete.
         */
        inline fun <reified T : Verticle> deployVerticle(vertx: Vertx, options: DeploymentOptions, completionHandler: Handler<AsyncResult<String>>) {
            vertx.deployVerticle(getFullVerticleName<T>(), options, completionHandler)
        }

        /**
         * Gets the name of the verticle with adding prefix required to notify vertx to use
         * @{@link GuiceVerticleFactory} factory for verticle creation.
         *
         * @param verticleClazz the class of the verticle to deploy.
         * @return Name of the verticle which can be used for deployment to vertx.
         */
        inline fun <reified T : Verticle> getFullVerticleName(): String {
            return PREFIX + ":" + T::class.java.canonicalName
        }

    }

}