package com.ufoscout.vertxk.kodein.json

import com.ufoscout.coreutils.json.kotlin.JsonSerializerService
import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.kodein.VertxK
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.kodein.di.direct
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.time.LocalDate
import java.util.*

class JsonModuleTest : BaseTest() {

    private var jsonSerializerService: JsonSerializerService? = null

    @Before
    fun setup() = runBlocking {
        val kodein = VertxK.start(vertx, JsonModule())
        jsonSerializerService = kodein.direct.instance<JsonSerializerService>()
    }

    @Test
    fun jsonServiceShouldNotBeNull() {
        assertNotNull(jsonSerializerService)
    }

    @Test
    fun testJson() {
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
    @Throws(UnsupportedEncodingException::class)
    fun testJsonOutputStream() {
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
