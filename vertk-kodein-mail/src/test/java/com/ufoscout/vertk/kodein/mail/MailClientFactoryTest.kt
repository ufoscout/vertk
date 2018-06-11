package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.BaseIT
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
        assertTrue(MailClientFactory.build(mailConfig, vertk()) is VertxMailClient)
    }

    @Test
    fun shouldReturnNoOpsMailClientByDefault() {
        val mailConfig = MailConfig(
                clientType = ""
        )
        assertTrue(MailClientFactory.build(mailConfig, vertk()) is NoOpsMailClient)
    }

}