package com.ufoscout.vertk.kodein.mail

import com.ufoscout.vertk.kodein.VertkKodeinModule
import io.vertx.core.Vertx
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MailModule(private val mailConfig: MailConfig): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<MailClient>() with singleton { MailClientFactory.build(mailConfig, instance()) }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
    }

}