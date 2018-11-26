package com.ufoscout.vertk.kodein.mail

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult

/**
 * A MailClient decorator implementation that keeps all emails in memory
 * and forward the publish call to the decorated MailClient.
 * This is useful for unit testing.
 */
class InMemoryMailClientDecorator(val decoratedMailClient: MailClient = NoOpsMailClient()) : MailClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    val receivedEmails = mutableListOf<MailMessage>();

    init {
        logger.warn("Configure InMemoryMailClientDecorator. All emails will be kept in memory and never sent!")
    }

    override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
        logger.info("Email not sent: ${mailMessage.toJson()}")
        receivedEmails.add(mailMessage)
        return decoratedMailClient.sendEmail(mailMessage)
    }

}