package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.BaseTest
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class InMemoryMailClientTest: BaseTest() {

    @Test
    fun shouldKeepAllEmailsInMemory() = runBlocking<Unit> {
        val client = InMemoryMailClientDecorator()
        val message = MailMessage()
        client.sendEmail(message)
        assertTrue(client.receivedEmails.contains(message))
    }

    @Test
    fun shouldCallTheDecoratedMailClient() = runBlocking<Unit> {
        val mock = MockClient()
        val client = InMemoryMailClientDecorator(mock)
        val message = MailMessage()

        val result = client.sendEmail(message)

        assertTrue(mock.called)
        assertEquals(message, mock.calledWith)
        assertEquals(mock.result, result)
    }

    class MockClient : MailClient {
        var called = false
        var calledWith: MailMessage? = null
        val result = MailResult()

        override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
            called = true
            calledWith = mailMessage
            return result
        }

    }

}