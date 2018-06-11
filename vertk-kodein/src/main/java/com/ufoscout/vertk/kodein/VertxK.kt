package com.ufoscout.vertk.kodein

import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.file.FileSystem
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.shareddata.SharedData
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.allInstances
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule

object VertxK {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun start(vertk: Vertx,
                      vararg modules: VertxKModule): Kodein {

        log.info("Vertxk initialization start...")

        val kodein = Kodein {
            import(jxInjectorModule)
            bind<Vertx>() with singleton { vertk }
            bind<EventBus>() with singleton { vertk.eventBus() }
            bind<FileSystem>() with singleton { vertk.fileSystem() }
            bind<SharedData>() with singleton { vertk.sharedData() }
            build(this, *modules)
        }

        log.info("Initialized VertxKComponents...")
        val kdirect = kodein.direct
        val instances: List<VertxKComponent> = kdirect.allInstances()
        instances.forEach { it.start() }

        log.info("VertxKComponents started")

        vertk.registerVerticleFactory(VertxkKodeinVerticleFactory(kodein))

        log.info("Initialized VertxKModules...")
        for (module in modules) {
            log.info("Initialize VertxKModule [${module.javaClass.name}]")
            module.onInit(vertk, kodein)
        }

        log.info("Vertxk initialization completed")

        return kodein
    }

    private fun build(builder: Kodein.MainBuilder, vararg modules: VertxKModule) {
        for (module in modules) {
            //Vertxk.log.debug("Import Kodein Module from ${module.javaClass.name}")
            builder.import(module.module(), allowOverride = true)
        }
    }

}