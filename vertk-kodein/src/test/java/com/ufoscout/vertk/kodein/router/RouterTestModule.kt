package com.ufoscout.vertk.kodein.router

import com.ufoscout.vertk.kodein.VertxKModule
import com.ufoscout.vertk.kodein.deployKodeinVerticle
import io.vertx.core.Vertx
import org.kodein.di.Kodein

class RouterTestModule: VertxKModule {

    override fun module() = Kodein.Module {}

    override suspend fun onInit(vertk: Vertx, kodein: Kodein) {
        vertk.deployKodeinVerticle<TestWebController>()
    }

}