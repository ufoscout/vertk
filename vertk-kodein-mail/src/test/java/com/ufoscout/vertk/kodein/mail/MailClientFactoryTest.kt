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
        val client = MailClientFactory.build(mailConfig, vertk())
        assertTrue(client is InMemoryMailClientDecorator)
        assertTrue((client as InMemoryMailClientDecorator).decoratedMailClient is NoOpsMailClient)
    }

    @Test
    fun shouldReturnInMemoryMailClientDecoratorForVertx() {
        val mailConfig = MailConfig(
                clientType = MailClientFactory.IN_MEMORY_VERTX
        )
        val client = MailClientFactory.build(mailConfig, vertk())
        assertTrue(client is InMemoryMailClientDecorator)
        assertTrue((client as InMemoryMailClientDecorator).decoratedMailClient is VertxMailClient)
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