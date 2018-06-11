package com.ufoscout.vertk.kodein.stub

import com.ufoscout.vertk.kodein.VertxKModule
import com.ufoscout.vertk.kodein.deployKodeinVerticle
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

    override suspend fun onInit(vertk: Vertx, kodein: Kodein) {
        vertk.deployKodeinVerticle<VertxKVerticleImpl>(deploymentOptions)
    }

}