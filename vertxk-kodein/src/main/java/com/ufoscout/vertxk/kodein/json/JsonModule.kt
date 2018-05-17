package com.ufoscout.vertxk.kodein.json

import com.ufoscout.vertxk.kodein.VertxKModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class JsonModule(): VertxKModule {

    override fun module() = Kodein.Module {
            bind<JsonSerializerService>() with singleton {JacksonJsonSerializerService()}
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}
