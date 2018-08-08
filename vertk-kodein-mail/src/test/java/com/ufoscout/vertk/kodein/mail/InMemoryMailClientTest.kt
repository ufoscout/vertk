package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.BaseTest
import io.vertx.ext.mail.MailMessage
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class InMemoryMailClientTest: BaseTest() {

    @Test
    fun shouldKeepAllEmailsInMemory() = runBlocking<Unit> {
        val client = InMemoryMailClient()
        val message = MailMessage()
        client.sendEmail(message)
        assertTrue(client.receivedEmails.contains(message))
    }

}