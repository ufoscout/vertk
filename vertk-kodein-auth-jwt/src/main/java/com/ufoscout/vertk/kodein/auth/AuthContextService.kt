package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.vertk.kodein.VertxKComponent
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService: VertxKComponent {

    fun get(routingContext: RoutingContext): AuthContext<Long> {
        return get(routingContext.request())
    }

    fun get(httpServerRequest: HttpServerRequest): AuthContext<Long>

    fun <R> generateToken(auth: Auth<R>): String
}