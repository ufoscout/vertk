package com.ufoscout.vertxk.kodein.stub

import com.ufoscout.vertxk.kodein.VertxKModule
import com.ufoscout.vertxk.kodein.deployKodeinVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.*

class StubModule(val deploymentOptions: DeploymentOptions = DeploymentOptions()): VertxKModule {

    companion object {
        val RANDOM_NAME= UUID.randomUUID().toString()
    }

    override fun module(): Kodein.Module {
        return Kodein.Module{
            bind<String>() with singleton { RANDOM_NAME }
            bind<VertxKComponentImpl>() with singleton { VertxKComponentImpl(instance()) }
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.deployKodeinVerticle<VertxKVerticle>(deploymentOptions)
    }

}