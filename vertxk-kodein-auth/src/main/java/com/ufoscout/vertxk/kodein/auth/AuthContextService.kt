package com.ufoscout.vertxk.kodein.auth

import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.vertxk.kodein.VertxKComponent
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService: VertxKComponent {

    fun get(routingContext: RoutingContext): AuthContext<Long> {
        return get(routingContext.request())
    }

    fun get(httpServerRequest: HttpServerRequest): AuthContext<Long>

}