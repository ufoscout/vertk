package com.ufoscout.vertk.kodein

import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.vertk.kodein.web.AuthenticationController
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AuthTestModule(): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<RolesProvider>() with singleton { InMemoryRolesProvider() }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.awaitDeployKodeinVerticle<AuthenticationController>()
    }

}