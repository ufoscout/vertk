package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService

class UserAuthServiceImpl(
        private val _authService: AuthService<Long, User>,
        private val _jwtService: JwtService
    ) : UserAuthService, AuthContextServiceImpl<Long, User>(
        _authService,
        _jwtService,
        User::class,
        {User(-1L, "", 0L)}
    ) {

}