package com.ufoscout.vertxk.kodein.mail

import io.vertx.core.Vertx

object MailClientFactory {

    val VERTX = "vertx"

    fun build(mailConfig: MailConfig, vertx: Vertx): MailClient {
        return when (mailConfig.clientType) {
            VERTX -> VertxMailClient(mailConfig, vertx)
            else -> { // Note the block
                NoOpsMailClient()
            }
        }
    }

}