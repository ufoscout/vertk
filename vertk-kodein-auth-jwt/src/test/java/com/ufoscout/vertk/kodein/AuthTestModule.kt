package com.ufoscout.vertk.kodein

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.kodein.web.AuthenticationController
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AuthTestModule(): VertxKModule {

    override fun module() = Kodein.Module {
        bind<RolesProvider>() with singleton { InMemoryRolesProvider() }
    }

    override suspend fun onInit(vertk: Vertx, kodein: Kodein) {
        vertk.deployKodeinVerticle<AuthenticationController>()
    }

}