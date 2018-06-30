package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import io.vertx.core.http.HttpServerRequest

open class AuthContextServiceImpl(
        val authService: AuthService,
        val jwtService: JwtService): AuthContextService {

    override fun tokenFrom(httpServerRequest: HttpServerRequest): String? {
        val header = httpServerRequest.getHeader(AuthContants.JWT_TOKEN_HEADER);
        if (header!=null && header.startsWith(AuthContants.JWT_TOKEN_HEADER_SUFFIX)) {
            return header.substring(AuthContants.JWT_TOKEN_HEADER_SUFFIX.length)
        }
        return null
    }

    override suspend fun start() {
        authService.start()
    }

    override fun from(auth: Auth): AuthContext {
        return authService.auth(auth)
    }

    override fun from(request: HttpServerRequest): AuthContext {
        val token = tokenFrom(request)
        if (token!=null) {
            val user: Auth = jwtService.parse(token, Auth::class)
            return from(user)
        }
        return from(Auth())
    }

    override fun generateToken(auth: Auth): String {
        return jwtService.generate(auth.username, auth)
    }

}