package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.web.*
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.coroutines.CoroutineVerticle

class AuthenticationController (val routerService: RouterService,
                                val auth: AuthContextService): CoroutineVerticle() {

    companion object {
        val BASE_AUTH_API = "/auth"
        val userService = InMemoryUserService()
    }

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {

        val router = routerService.router()

        router.awaitRestPost<LoginDto>(BASE_AUTH_API + "/login") { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            logger.info("Return token: [${token}]")
            LoginResponseDto(token)
        }

        router.awaitRestGet(BASE_AUTH_API + "/test/public") {
            val authContext = auth.from(it.request())
            authContext.auth
        }

        router.awaitRestGet(BASE_AUTH_API + "/test/authenticated") {
            val authContext = auth.from(it).isAuthenticated()
            authContext.auth
        }

        router.awaitRestGet(BASE_AUTH_API + "/test/protected") {
            val authContext = auth.from(it).hasRole("ADMIN")
            authContext.auth
        }

    }

}