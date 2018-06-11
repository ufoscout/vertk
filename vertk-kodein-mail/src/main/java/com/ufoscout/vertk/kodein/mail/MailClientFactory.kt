package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.Vertk

object MailClientFactory {

    val VERTX = "vertk"

    fun build(mailConfig: MailConfig, vertk: Vertk): MailClient {
        return when (mailConfig.clientType) {
            VERTX -> VertxMailClient(mailConfig.config, vertk)
            else -> { // Note the block
                NoOpsMailClient()
            }
        }
    }

}