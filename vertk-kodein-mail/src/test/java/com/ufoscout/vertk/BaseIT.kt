package com.ufoscout.vertk

import com.ufoscout.vertk.kodein.VertkKodein
import com.ufoscout.vertk.kodein.mail.MailClientFactory
import com.ufoscout.vertk.kodein.mail.MailConfig
import com.ufoscout.vertk.kodein.mail.MailModule
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import org.testcontainers.containers.GenericContainer

abstract class BaseIT : BaseTest() {

    companion object {

        private var vertk: Vertk? = null
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

            vertk = Vertk.vertk()

            kodein = VertkKodein.start(
                    vertk!!,
                    MailModule(mailConfig!!)
            ).direct

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            vertk!!.close()
        }

    }

    protected fun vertk(): Vertk = vertk!!
    protected fun mailConfig() = mailConfig!!
    protected fun kodein(): DKodein = kodein!!

}

class MailhogContainer(val dockerImage: String): GenericContainer<MailhogContainer>(dockerImage) {}