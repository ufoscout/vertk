package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertxKVerticle
import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.kodein.auth.auth
import com.ufoscout.vertk.kodein.router.RouterService
import io.vertx.core.logging.LoggerFactory

class AuthenticationController (val routerService: RouterService,
                                val auth: AuthContextService): VertxKVerticle() {

    companion object {
        val BASE_AUTH_API = "/auth"
        val userService = InMemoryUserService()
    }

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun start() {

        val router = routerService.router()

        router.restPost(BASE_AUTH_API + "/login", LoginDto::class) { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            logger.info("Return token: [${token}]")
            LoginResponseDto(token)
        }

        router.get(BASE_AUTH_API + "/test/public").handler {
            val authContext = auth.get(it.request())
            it.request().response().endWithJson(authContext.auth)
        }

        router.get(BASE_AUTH_API + "/test/authenticated").handler {
            val authContext = it.auth().isAuthenticated()
            it.request().response().endWithJson(authContext.auth)
        }

        router.get(BASE_AUTH_API + "/test/protected").handler {
            val authContext = auth.get(it).hasRole("ADMIN")
            it.request().response().endWithJson(authContext.auth)
        }

    }

}