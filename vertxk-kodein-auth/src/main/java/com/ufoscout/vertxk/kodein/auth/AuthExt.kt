package com.ufoscout.vertxk.kodein.auth

import com.ufoscout.coreutils.auth.AuthContext
import io.vertx.ext.web.RoutingContext

interface AuthExt {
}

fun RoutingContext.auth() : AuthContext<Long> {
    return this.vertx().sharedData().getLocalMap<String, AuthModule.AuthShareableData>(
            AuthContants.LOCAL_DATA_AUTH_SERVICE_MAP
            ).get(AuthContants.LOCAL_DATA_AUTH_SERVICE_KEY)!!
            .authContextService
            .get(this)
}