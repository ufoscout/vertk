package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.auth.Role
import com.ufoscout.vertk.kodein.VertkKodeinStartable
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService<R, U: Auth<R>>: VertkKodeinStartable {

    fun get(routingContext: RoutingContext): AuthContext<R, U> {
        return get(routingContext.request())
    }

    fun get(httpServerRequest: HttpServerRequest): AuthContext<R, U>

    fun generateToken(auth: U): String

    /**
     * Returns the encoded representation of a set of [Role]s.
     *
     * @param roleNames
     * @return
     */
    fun encode(vararg roleNames: String): R

}