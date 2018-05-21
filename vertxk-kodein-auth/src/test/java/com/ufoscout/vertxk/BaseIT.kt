package com.ufoscout.vertxk

import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.vertxk.kodein.AuthTestModule
import com.ufoscout.vertxk.kodein.VertxK
import com.ufoscout.vertxk.kodein.auth.AuthModule
import com.ufoscout.vertxk.kodein.config.RouterConfig
import com.ufoscout.vertxk.kodein.json.JsonModule
import com.ufoscout.vertxk.kodein.router.RouterModule
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

        private var vertx: Vertx? = null
        private val port: Int = getFreePort()
        private var kodein: DKodein? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            System.setProperty("server.port", port.toString())
            vertx = Vertx.vertx()

            kodein = VertxK.start(
                    vertx!!,
                    AuthModule(JwtConfig("secret", "HS512", 60)),
                    JsonModule(),
                    AuthTestModule(),
                    RouterModule(RouterConfig(port))
            ).direct

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            awaitResult<Void> { vertx!!.close(it) }
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

    protected fun vertx(): Vertx = vertx!!

    protected fun kodein(): DKodein = kodein!!

}
