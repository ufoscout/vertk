package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.BaseIT
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
    fun shouldReturnNoOpsMailClient() {
        val mailConfig = MailConfig(
                clientType = MailClientFactory.NO_OPS
        )
        assertTrue(MailClientFactory.build(mailConfig, vertk()) is NoOpsMailClient)
    }

    @Test
    fun shouldReturnInMemoryMailClient() {
        val mailConfig = MailConfig(
                clientType = MailClientFactory.IN_MEMORY
        )
        assertTrue(MailClientFactory.build(mailConfig, vertk()) is InMemoryMailClient)
    }

    @Test
    fun shouldFailIfUnknownMailClient() {
        assertThrows<RuntimeException> {
            val mailConfig = MailConfig(
                    clientType = ""
            )
            MailClientFactory.build(mailConfig, vertk())
        }
    }

}