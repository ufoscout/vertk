package com.ufoscout.vertk

import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.vertk.kodein.AuthTestModule
import com.ufoscout.vertk.kodein.VertkKodein
import com.ufoscout.vertk.kodein.auth.AuthModule
import com.ufoscout.vertk.kodein.config.RouterConfig
import com.ufoscout.vertk.kodein.json.JsonModule
import com.ufoscout.vertk.kodein.router.RouterModule
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.kodein.di.DKodein
import org.kodein.di.direct
import java.io.IOException
import java.net.ServerSocket

abstract class BaseIT : BaseTest() {

    companion object {

        private var vertk: Vertk? = null
        private val port: Int = getFreePort()
        private var kodein: DKodein? = null

        @BeforeAll @JvmStatic
        fun setUpClass() = runBlocking<Unit> {

            System.setProperty("server.port", port.toString())
            vertk = Vertk.vertk()

            kodein = VertkKodein.start(
                    vertk!!,
                    AuthModule(JwtConfig("secret", "HS512", 60)),
                    JsonModule(),
                    AuthTestModule(),
                    RouterModule(RouterConfig(port))
            ).direct

        }

        @AfterAll @JvmStatic
        fun tearDownClass() = runBlocking<Unit> {
            vertk!!.close()
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

    protected fun vertk(): Vertk = vertk!!

    protected fun kodein(): DKodein = kodein!!

}
