package com.ufoscout.vertk.kodein.mail

import io.vertx.core.Vertx

object MailClientFactory {

    val VERTX = "vertk"

    fun build(mailConfig: MailConfig, vertk: Vertx): MailClient {
        return when (mailConfig.clientType) {
            VERTX -> VertxMailClient(mailConfig.config, vertk)
            else -> { // Note the block
                NoOpsMailClient()
            }
        }
    }

}