package com.ufoscout.vertk.kodein.mail

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult

/**
 * A MailClient implementation that keeps all emails in memory.
 * This is useful for unit testing.
 */
class InMemoryMailClient : MailClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    val receivedEmails = mutableListOf<MailMessage>();

    init {
        logger.warn("Configure InMemoryMailClient. All emails will be kept in memory and never sent!")
    }

    override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
        logger.info("Email not sent: ${mailMessage.toJson()}")
        receivedEmails.add(mailMessage)
        return MailResult()
    }

}