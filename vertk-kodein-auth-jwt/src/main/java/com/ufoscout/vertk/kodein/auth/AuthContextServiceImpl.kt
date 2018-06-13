package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import io.vertx.core.http.HttpServerRequest
import kotlin.reflect.KClass

open class AuthContextServiceImpl<R, U: Auth<R>>(
        val authService: AuthService<R, U>,
        val jwtService: JwtService,
        val klass: KClass<U>,
        val producer: () -> U ) : AuthContextService<R, U> {

    override suspend fun start() {
        authService.start()
    }

    override fun get(request: HttpServerRequest): AuthContext<R, U> {
        val header = request.getHeader(AuthContants.JWT_TOKEN_HEADER);
        if (header!=null && header.startsWith(AuthContants.JWT_TOKEN_HEADER_SUFFIX)) {
            val user: U = jwtService.parse(header.substring(AuthContants.JWT_TOKEN_HEADER_SUFFIX.length), klass)
            return authService.auth(user)
        }
        return authService.auth(producer.invoke())
    }

    override fun generateToken(auth: U): String {
        return jwtService.generate(auth.username, auth)
    }

    override fun encode(vararg roleNames: String): R {
        return authService.encode(*roleNames)
    }

}