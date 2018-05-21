package com.ufoscout.vertxk.kodein.json

import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.kodein.VertxK
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.util.*

class JsonModuleTest : BaseTest() {

    companion object {

        private var jsonSerializerService: JsonSerializerService? = null
        private var vertx: Vertx? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            vertx = Vertx.vertx()

            val kodein = VertxK.start(vertx!!, JsonModule())
            jsonSerializerService = kodein.direct.instance<JsonSerializerService>()

            println("CREATED")
            println("CREATED")
            println("CREATED")
            println("CREATED")
            println("CREATED")
            println("CREATED")
            println("CREATED")
        }

    }

    @Test
    fun jsonServiceShouldNotBeNull() {
        assertNotNull(jsonSerializerService)
    }

    @Test
    fun testJson() {
        assertNotNull(jsonSerializerService)

        val message = SerializerBean()
        message.id = SecureRandom().nextLong()
        message.name = UUID.randomUUID().toString()
        message.date = LocalDate.now()

        val json = jsonSerializerService!!.toJson(message)
        assertNotNull(json)
        assertTrue(json.contains("" + message.id))

        println("JSON content: /n[${json}]")

        val fromJson: SerializerBean = jsonSerializerService!!.fromJson(json)
        assertNotNull(fromJson)
        assertEquals(message.id, fromJson.id)
        assertEquals(message.date, fromJson.date)
        assertEquals(message.name, fromJson.name)

    }

    @Test
    fun testJsonOutputStream() {
        assertNotNull(jsonSerializerService)

        val message = SerializerBean()
        message.id = SecureRandom().nextLong()
        message.name = UUID.randomUUID().toString()
        message.date = LocalDate.now()

        val baos = ByteArrayOutputStream()

        jsonSerializerService!!.toPrettyPrintedJson(message, baos)

        val json = baos.toString(StandardCharsets.UTF_8.name())

        assertNotNull(json)
        assertTrue(json.contains("" + message.id))

        println("JSON content: /n[${json}]")

        val fromJson: SerializerBean = jsonSerializerService!!.fromJson(json)
        assertNotNull(fromJson)
        assertEquals(message.id, fromJson.id)
        assertEquals(message.date, fromJson.date)
        assertEquals(message.name, fromJson.name)

    }

}
