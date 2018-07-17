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

object VertkKodein {

    private val log = LoggerFactory.getLogger(javaClass)

    suspend fun start(vertx: Vertx,
                      vararg modules: VertkKodeinModule): Kodein {

        log.info("Vertxk initialization start...")

        val kodein = Kodein {
            import(jxInjectorModule)
            bind<Vertx>() with singleton { vertx }
            bind<EventBus>() with singleton { vertx.eventBus() }
            bind<FileSystem>() with singleton { vertx.fileSystem() }
            bind<SharedData>() with singleton { vertx.sharedData() }
            build(this, *modules)
        }

        log.info("Initialized VertxKComponents...")
        val kdirect = kodein.direct
        val instances: List<VertkKodeinStartable> = kdirect.allInstances()
        instances.forEach { it.start() }

        log.info("VertxKComponents started")

        vertx.registerVerticleFactory(VertkKodeinVerticleFactory(kodein))

        log.info("Initialized VertxKModules...")
        for (module in modules) {
            log.info("Initialize VertkKodeinModule [${module.javaClass.name}]")
            module.onInit(vertx, kodein)
        }

        log.info("Vertxk initialization completed")

        return kodein
    }

    private fun build(builder: Kodein.MainBuilder, vararg modules: VertkKodeinModule) {
        for (module in modules) {
            //Vertxk.log.debug("Import Kodein Module from ${module.javaClass.name}")
            builder.import(module.module(), allowOverride = true)
        }
    }

}