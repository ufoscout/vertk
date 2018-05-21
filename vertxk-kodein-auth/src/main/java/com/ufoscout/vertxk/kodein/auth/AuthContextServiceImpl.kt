package com.ufoscout.vertxk.kodein.auth

import com.ufoscout.coreutils.auth.AuthContext
import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import io.vertx.core.http.HttpServerRequest

class AuthContextServiceImpl(val authService: AuthService<Long>, val jwtService: JwtService) : AuthContextService {

    override suspend fun start() {
        authService.start()
    }

    override fun get(request: HttpServerRequest): AuthContext<Long> {
        val header = request.getHeader(AuthContants.JWT_TOKEN_HEADER);
        if (header!=null && header.startsWith(AuthContants.JWT_TOKEN_HEADER_SUFFIX)) {
            val user: User = jwtService.parse(header.substring(AuthContants.JWT_TOKEN_HEADER_SUFFIX.length), User::class)
            return authService.auth(user)
        }
        return return authService.auth(User("", 0L))
    }

}