package com.ufoscout.vertxk.kodein.json

import io.vertx.core.json.Json
import java.io.IOException
import java.io.OutputStream
import kotlin.reflect.KClass


/**
 *
 * @author Francesco Cina'
 */
class JacksonJsonSerializerService() : JsonSerializerService {

    override suspend fun start() {
        Json.mapper = JacksonMapperFactory.mapper
        Json.prettyMapper = JacksonMapperFactory.prettyMapper
    }

    override fun toJson(obj: Any): String {
        try {
            return JacksonMapperFactory.mapper.writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toJson(obj: Any, out: OutputStream) {
        try {
            JacksonMapperFactory.mapper.writeValue(out, obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(obj: Any): String {
        try {
            return JacksonMapperFactory.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun toPrettyPrintedJson(obj: Any, out: OutputStream) {
        try {
            JacksonMapperFactory.mapper.writerWithDefaultPrettyPrinter().writeValue(out, obj)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun <T: Any> fromJson(clazz: KClass<T>, json: String): T {
        try {
            return JacksonMapperFactory.mapper.readValue(json, clazz.java)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

}
