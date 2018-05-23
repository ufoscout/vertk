package com.ufoscout.vertxk.kodein.mail

import com.ufoscout.vertxk.BaseIT
import io.vertx.ext.mail.MailMessage
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance

internal class MailServiceImplIt: BaseIT() {

    private val mailClient = kodein().instance<MailClient>()

    @Test
    fun mailServiceShouldExist() {
        assertNotNull(mailClient)
    }

    @Test
    fun shouldSendEmail() = runBlocking{

        val to = "recipient@example.org"
        val cc1 = "Another User <another@example.net>"
        val cc2 = "Another Another User <another-another@example.net>"

        var message = MailMessage()
        message.from = "user@example.com (Example User)"
        message.to = listOf(to)
        message.cc = listOf(cc1, cc2)
        message.text = "this is the plain message text"
        message.html = "this is html text <a href=\"http://vertx.io\">vertx.io</a>"

        val result = mailClient.sendEmail(message)
        logger.info("Mail sent. Result: ${result}")
        assertNotNull(result.messageID)
        assertEquals(3, result.recipients.size)
        assertTrue(result.recipients.contains(to))
        assertTrue(result.recipients.contains("another@example.net"))
        assertTrue(result.recipients.contains("another-another@example.net"))
    }

}