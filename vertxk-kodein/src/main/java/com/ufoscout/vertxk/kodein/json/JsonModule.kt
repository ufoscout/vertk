package com.ufoscout.vertxk.kodein.json

import com.ufoscout.coreutils.json.JacksonJsonSerializerService
import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.vertxk.kodein.VertxKModule
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class JsonModule(): VertxKModule {

    override fun module() = Kodein.Module {
            bind<JsonSerializerService>() with singleton {
                JsonSerializerService(JacksonJsonSerializerService(JacksonMapperFactory.mapper))
            }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        Json.mapper = JacksonMapperFactory.mapper
        Json.prettyMapper = JacksonMapperFactory.prettyMapper
    }

}
