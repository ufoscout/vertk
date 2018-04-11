package com.ufoscout.vertxk

import com.ufoscout.vertxk.util.VertxkKodein
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import kotlinx.coroutines.experimental.runBlocking
import org.kodein.di.Kodein
import org.kodein.di.jxinject.jxInjectorModule

object Vertxk {

    private val log = LoggerFactory.getLogger(Vertxk::class.java)

    fun start(vertx: Vertx, vararg modules: VertxkModule): Kodein {
        return runBlocking {
            launch(vertx, *modules)
        }
    }

    suspend fun launch(vertx: Vertx, vararg modules: VertxkModule): Kodein {

        log.info("Vertxk initialization start...")

        val kodein = Kodein {
            import(jxInjectorModule)
            import(VertxkKodein.module(vertx))

            build(this, *modules)
        }

        VertxkKodein.registerFactory(vertx, kodein)

        for (module in modules) {
            log.debug("Initialize Module from ${module.javaClass.name}")
            module.onInit(vertx, kodein)
        }

        log.info("Vertxk initialization completed")

        return kodein
    }

    private fun build(builder: Kodein.MainBuilder, vararg modules: VertxkModule) {
        for (module in modules) {
            log.debug("Import Kodein Module from ${module.javaClass.name}")
            builder.import(module.module(), allowOverride = true)
        }
    }
}