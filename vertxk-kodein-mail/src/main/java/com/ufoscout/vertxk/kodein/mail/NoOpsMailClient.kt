package com.ufoscout.vertxk.kodein.mail

import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult

class NoOpsMailClient : MailClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    init {
        logger.warn("Configure NoOpsMailClient. No emails will be sent!")
    }

    override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
        logger.info("Email not sent: ${mailMessage.toJson()}")
        return MailResult()
    }

}