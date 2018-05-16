package com.ufoscout.vertxk.kodein.router

import com.ufoscout.vertxk.kodein.VertxKModule
import com.ufoscout.vertxk.kodein.config.RouterConfig
import com.weweb.core.json.JacksonJsonSerializerService
import com.weweb.core.json.JsonSerializerService
import com.weweb.core.service.RouterService
import com.weweb.core.service.RouterServiceImpl
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class RouterModule(val routerConfig: RouterConfig): VertxKModule {

    override fun module() = Kodein.Module {
            bind<RouterConfig>() with singleton { routerConfig }
            bind<JsonSerializerService>() with singleton { JacksonJsonSerializerService() }
            bind<WebExceptionService>() with singleton { WebExceptionServiceImpl() }
            bind<RouterService>() with singleton { RouterServiceImpl(instance(), instance(), instance()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
