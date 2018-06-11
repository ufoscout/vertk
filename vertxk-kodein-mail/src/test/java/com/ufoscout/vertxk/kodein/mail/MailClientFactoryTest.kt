package com.ufoscout.vertxk.kodein.mail

import com.ufoscout.vertxk.BaseIT
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MailClientFactoryIT: BaseIT() {

    private val globalConfig = mailConfig()

    @Test
    fun shouldReturnVertxMailClient() {
        val mailConfig = MailConfig(
                clientType = MailClientFactory.VERTX,
                config = globalConfig.config
        )
        assertTrue(MailClientFactory.build(mailConfig, vertx()) is VertxMailClient)
    }

    @Test
    fun shouldReturnNoOpsMailClientByDefault() {
        val mailConfig = MailConfig(
                clientType = ""
        )
        assertTrue(MailClientFactory.build(mailConfig, vertx()) is NoOpsMailClient)
    }

}