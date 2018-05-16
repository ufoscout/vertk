package com.weweb.core.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.time.Instant

object JacksonMapperFactory {

    val mapper: ObjectMapper = jacksonObjectMapper()
    val prettyMapper: ObjectMapper = jacksonObjectMapper()

    init {
        initMapper(mapper)
        initMapper(prettyMapper)
        prettyMapper.configure(SerializationFeature.INDENT_OUTPUT, true)
    }

    private fun initMapper(mapper: ObjectMapper) {
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true)
        mapper.registerModule(JavaTimeModule())
        mapper.registerModule(Jdk8Module())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
    }

}