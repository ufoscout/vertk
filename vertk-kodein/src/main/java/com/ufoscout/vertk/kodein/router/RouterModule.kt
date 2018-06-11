package com.ufoscout.vertk.kodein.router

import com.ufoscout.vertk.kodein.VertxKModule
import com.ufoscout.vertk.kodein.config.RouterConfig
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RouterModule(val routerConfig: RouterConfig): VertxKModule {

    override fun module() = Kodein.Module {
            bind<RouterConfig>() with singleton { routerConfig }
            bind<WebExceptionService>() with singleton { WebExceptionServiceImpl() }
            bind<RouterService>() with singleton { RouterServiceImpl(instance(), instance(), instance()) }
    }

    override suspend fun onInit(vertk: Vertx, kodein: Kodein) {
    }

}
