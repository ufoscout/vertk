package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.UnauthenticatedException
import com.ufoscout.coreutils.auth.UnauthorizedException
import com.ufoscout.coreutils.jwt.TokenExpiredException
import com.ufoscout.vertk.kodein.VertkKodeinStartable
import com.ufoscout.vertk.kodein.web.WebException
import com.ufoscout.vertk.kodein.web.WebExceptionService
import com.ufoscout.vertk.kodein.web.registerTransformer

class AuthSetup(val webExceptionService: WebExceptionService): VertkKodeinStartable {

    override suspend fun start() {
        webExceptionService.registerTransformer<UnauthenticatedException>({ ex -> WebException(code = 401, message = "NotAuthenticated") })
        webExceptionService.registerTransformer<BadCredentialsException>({ ex -> WebException(code = 401, message = "BadCredentials") })
        webExceptionService.registerTransformer<UnauthorizedException>({ ex -> WebException(code = 403, message = "AccessDenied") })
        webExceptionService.registerTransformer<TokenExpiredException>({ exp -> WebException(code = 401, message = "TokenExpired") })
    }

}

