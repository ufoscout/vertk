package com.ufoscout.vertxk

import com.ufoscout.vertxk.kodein.VertxK
import com.ufoscout.vertxk.kodein.mail.MailClientFactory
import com.ufoscout.vertxk.kodein.mail.MailConfig
import com.ufoscout.vertxk.kodein.mail.MailModule
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.testcontainers.containers.GenericContainer

abstract class BaseIT : BaseTest(), K {

    companion object {

        private var vertx: Vertx? = null
        private var kodein: DKodein? = null
        private var mailConfig: MailConfig? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            val mh = MailhogContainer("mailhog/mailhog:latest")
                    .withExposedPorts(1025)
            mh!!.start()

            mailConfig = MailConfig(
                    clientType = MailClientFactory.VERTX,
                    config = io.vertx.ext.mail.MailConfig()
                            .setHostname(mh!!.getContainerIpAddress().toString())
                            .setPort(mh!!.getMappedPort(1025))
            )

            vertx = Vertx.vertx()

            kodein = VertxK.start(
                    vertx!!,
                    MailModule(mailConfig!!)
            ).direct

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            awaitResult<Void> { vertx!!.close(it) }
        }

    }

    protected fun vertx(): Vertx = vertx!!
    protected fun mailConfig() = mailConfig!!
    protected fun kodein(): DKodein = kodein!!

}

class MailhogContainer(val dockerImage: String): GenericContainer<MailhogContainer>(dockerImage) {}