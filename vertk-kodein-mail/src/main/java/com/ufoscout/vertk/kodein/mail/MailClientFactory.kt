package com.ufoscout.vertk.kodein.mail

import io.vertx.core.Vertx

object MailClientFactory {

    val VERTX = "vertx"
    val IN_MEMORY = "inMemory"
    val IN_MEMORY_VERTX = "inMemoryVertx"
    val NO_OPS = "noOps"

    fun build(mailConfig: MailConfig, vertx: Vertx): MailClient {
        return when (mailConfig.clientType) {
            VERTX -> VertxMailClient(mailConfig.config, vertx)
            IN_MEMORY -> InMemoryMailClientDecorator()
            IN_MEMORY_VERTX -> InMemoryMailClientDecorator(VertxMailClient(mailConfig.config, vertx))
            NO_OPS -> NoOpsMailClient()
            else -> { // Note the block
                throw RuntimeException("Bad configuration: Unknown Mail client type: [${mailConfig.clientType}]")
            }
        }
    }

}