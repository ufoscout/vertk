package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.UnauthenticatedException
import com.ufoscout.coreutils.auth.UnauthorizedException
import com.ufoscout.coreutils.jwt.TokenExpiredException
import com.ufoscout.vertk.kodein.VertkKodeinStartable
import com.ufoscout.vertk.kodein.web.WebException
import com.ufoscout.vertk.kodein.web.WebExceptionService
import com.ufoscout.vertk.kodein.web.registerTransformer
import io.netty.handler.codec.http.HttpResponseStatus

class AuthSetup(val webExceptionService: WebExceptionService): VertkKodeinStartable {

    override suspend fun start() {
        webExceptionService.registerTransformer<UnauthenticatedException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "NotAuthenticated") }
        webExceptionService.registerTransformer<BadCredentialsException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "BadCredentials") }
        webExceptionService.registerTransformer<UnauthorizedException> { ex -> WebException(cause = ex, code = HttpResponseStatus.FORBIDDEN.code(), message = "AccessDenied") }
        webExceptionService.registerTransformer<TokenExpiredException> { ex -> WebException(cause = ex, code = HttpResponseStatus.UNAUTHORIZED.code(), message = "TokenExpired") }
    }

}

