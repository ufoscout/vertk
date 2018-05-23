package com.ufoscout.vertxk.kodein.mail

import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult
import io.vertx.kotlin.coroutines.awaitResult

class VertxMailClient(private val mailConfig: MailConfig, private val vertx: Vertx): com.ufoscout.vertxk.kodein.mail.MailClient {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mailClient: MailClient

    init {
        var config = io.vertx.ext.mail.MailConfig()
        config.hostname = mailConfig.hostname
        config.port = mailConfig.port
        config.username = mailConfig.username
        config.password = mailConfig.password

        logger.info("Configure MailClient for server ${config.hostname}:${config.port} ")

        mailClient = MailClient.createNonShared(vertx, config)
    }

    override suspend fun sendEmail(mailMessage: MailMessage): MailResult {
        return awaitResult<MailResult> {
            mailClient.sendMail(mailMessage, it)
        }
    }

}