package com.ufoscout.vertk

import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.vertk.kodein.AuthTestModule
import com.ufoscout.vertk.kodein.VertxK
import com.ufoscout.vertk.kodein.auth.AuthModule
import com.ufoscout.vertk.kodein.config.RouterConfig
import com.ufoscout.vertk.kodein.json.JsonModule
import com.ufoscout.vertk.kodein.router.RouterModule
import io.vertx.core.Vertx
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import java.io.IOException
import java.net.ServerSocket

abstract class BaseIT : BaseTest(), K {

    companion object {

        private var vertk: Vertx? = null
        private val port: Int = getFreePort()
        private var kodein: DKodein? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            System.setProperty("server.port", port.toString())
            vertk = Vertx.vertx()

            kodein = VertxK.start(
                    vertk!!,
                    AuthModule(JwtConfig("secret", "HS512", 60)),
                    JsonModule(),
                    AuthTestModule(),
                    RouterModule(RouterConfig(port))
            ).direct

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            awaitResult<Void> { vertk!!.close(it) }
        }

        @Synchronized private fun getFreePort(): Int {
            try {
                ServerSocket(0).use { socket ->
                    socket.reuseAddress = true
                    return socket.localPort
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

    }

    protected fun port(): Int = port

    protected fun vertk(): Vertx = vertk!!

    protected fun kodein(): DKodein = kodein!!

}
