package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.auth.AuthContextService
import com.ufoscout.vertk.kodein.auth.auth
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

        router.restPost<LoginDto>(BASE_AUTH_API + "/login") { rc, loginDto ->
            val login = userService.login(loginDto.username, loginDto.password)
            val token = auth.generateToken(login)
            logger.info("Return token: [${token}]")
            LoginResponseDto(token)
        }


        router.restGet(BASE_AUTH_API + "/test/public") {
            val authContext = auth.get(it.request())
            authContext.auth
        }

        router.restGet(BASE_AUTH_API + "/test/authenticated") {
            val authContext = it.auth().isAuthenticated()
            authContext.auth
        }

        router.restGet(BASE_AUTH_API + "/test/protected") {
            val authContext = auth.get(it).hasRole("ADMIN")
            authContext.auth
        }

        /*
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
*/
    }

}