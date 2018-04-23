package com.ufoscout.vertxk.kodein

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.shareddata.SharedData
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.allInstances
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

internal class VertxKVerticle: CoroutineVerticle() {

    override suspend fun start() {

        val map = vertx.sharedData().getLocalMap<String, VertxKConfig>(VertxK.VERTXK_DATA_MAP)
        val config = map.get(VertxK.VERTXK_DATA_CONFIG)!!

        val kodein = Kodein {
            bind<Vertx>() with singleton { vertx }
            bind<EventBus>() with singleton { vertx.eventBus() }
            bind<FileSystem>() with singleton { vertx.fileSystem() }
            bind<SharedData>() with singleton { vertx.sharedData() }
            build(this, config.modules)
        }

        val kdirect = kodein.direct

        val instances: List<VertxKComponent> = kdirect.allInstances()
        instances.forEach { it.start() }

        config.kodein = kodein
    }

    private fun build(builder: Kodein.MainBuilder, modules: Array<Kodein.Module>) {
        for (module in modules) {
            //Vertxk.log.debug("Import Kodein Module from ${module.javaClass.name}")
            builder.import(module, allowOverride = true)
        }
    }
}