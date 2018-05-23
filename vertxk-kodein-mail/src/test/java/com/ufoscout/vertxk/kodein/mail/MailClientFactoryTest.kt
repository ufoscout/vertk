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
                hostname = globalConfig.hostname,
                port = globalConfig.port,
                username = globalConfig.username,
                password = globalConfig.password
        )
        assertTrue(MailClientFactory.build(mailConfig, vertx()) is VertxMailClient)
    }

    @Test
    fun shouldReturnNoOpsMailClientByDefault() {
        val mailConfig = MailConfig(
                clientType = "",
                hostname = "",
                port = 0,
                username = "",
                password = ""
        )
        assertTrue(MailClientFactory.build(mailConfig, vertx()) is NoOpsMailClient)
    }

}