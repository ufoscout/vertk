package com.ufoscout.vertxk.kodein.web

import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertxk.kodein.VertxKVerticle
import com.ufoscout.vertxk.kodein.auth.AuthContextService
import com.ufoscout.vertxk.kodein.auth.auth
import com.ufoscout.vertxk.kodein.router.RouterService

class AuthenticationController (val routerService: RouterService,
                                val jwt: JwtService,
                                val auth: AuthContextService): VertxKVerticle() {

    companion object {
        val BASE_AUTH_API = "/auth"
        val userService = InMemoryUserService()
    }

    override suspend fun start() {

        val router = routerService.router()

        router.restPost(BASE_AUTH_API + "/login", LoginDto::class) { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = jwt.generate(login.username, login)
            println("Return token: [${token}]")
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