package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.auth.Role
import com.ufoscout.vertk.kodein.VertkKodeinStartable
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.RoutingContext

interface AuthContextService<R, U: Auth<R>>: VertkKodeinStartable {

    fun tokenFrom(routingContext: RoutingContext): String? {
        return tokenFrom(routingContext.request())
    }

    fun tokenFrom(httpServerRequest: HttpServerRequest): String?

    fun from(routingContext: RoutingContext): AuthContext<R, U> {
        return from(routingContext.request())
    }

    fun from(httpServerRequest: HttpServerRequest): AuthContext<R, U>

    fun from(auth: U): AuthContext<R, U>

    fun generateToken(auth: U): String

    /**
     * Returns the encoded representation of a set of [Role]s.
     *
     * @param roleNames
     * @return
     */
    fun encode(vararg roleNames: String): R

    /**
     * Returns the set of [Role]s from the encoded representation.
     *
     * @param roleNames
     * @return
     */
    fun decode(encodedRoles: R): List<Role>

}