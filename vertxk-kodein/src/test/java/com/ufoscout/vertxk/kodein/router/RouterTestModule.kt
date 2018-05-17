package com.ufoscout.vertxk.kodein.router

import com.ufoscout.vertxk.kodein.VertxKModule
import com.ufoscout.vertxk.kodein.deployKodeinVerticle
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class RouterTestModule: VertxKModule {

    override fun module() = Kodein.Module {}

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.deployKodeinVerticle<TestWebController>()
    }

}