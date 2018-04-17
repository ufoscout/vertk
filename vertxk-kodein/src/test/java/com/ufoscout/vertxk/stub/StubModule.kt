package com.ufoscout.vertxk.stub

import com.ufoscout.vertxk.VertxkModule
import io.vertx.core.Vertx
import org.kodein.di.DKodein
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.util.*

class StubModule: VertxkModule() {

    companion object {
        val COROUTINE_VERTICLE_NAME = UUID.randomUUID().toString()
        var DEPLOYMENT_OPTIONS = io.vertx.core.DeploymentOptions()
        var MODULE_CALLED = false
        var ON_INIT_CALLED = false
    }

    init {
        MODULE_CALLED = false
        ON_INIT_CALLED = false
    }

    override fun module(): Kodein.Module {
        MODULE_CALLED = true
        return Kodein.Module{
            bind<String>() with singleton { COROUTINE_VERTICLE_NAME }
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: DKodein) {
        ON_INIT_CALLED = true
        deployVerticle<SimpleVerticle>(vertx)
        deployVerticle<CoroutinesVerticle>(vertx, DEPLOYMENT_OPTIONS)
    }
}