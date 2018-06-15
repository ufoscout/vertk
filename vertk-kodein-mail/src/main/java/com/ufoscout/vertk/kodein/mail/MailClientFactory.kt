package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.Vertk

object MailClientFactory {

    val VERTX = "vertx"
    val NO_OPS = "noOps"

    fun build(mailConfig: MailConfig, vertk: Vertk): MailClient {
        return when (mailConfig.clientType) {
            VERTX -> VertxMailClient(mailConfig.config, vertk)
            NO_OPS -> NoOpsMailClient()
            else -> { // Note the block
                throw RuntimeException("Bad configuration: Unknown Mail client type: [${mailConfig.clientType}]")
            }
        }
    }

}