package com.ufoscout.vertxk.kodein.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.auth.AuthServiceImpl
import com.ufoscout.coreutils.auth.RolesEncoderToLong
import com.ufoscout.coreutils.auth.RolesProvider
import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.coreutils.jwt.JwtServiceJJWT
import com.ufoscout.coreutils.jwt.kotlin.CoreJsonProvider
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertxk.kodein.VertxKModule
import io.vertx.core.Vertx
import io.vertx.core.shareddata.Shareable
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AuthModule(val jwtConfig: JwtConfig): VertxKModule {

    override fun module() = Kodein.Module {
        bind<AuthConfig>() with singleton { AuthConfig(instance()) }
        bind<AuthService<Long>>() with singleton {
            AuthServiceImpl<Long>(instance<RolesProvider>(), RolesEncoderToLong())
        }
        bind<JwtService>() with singleton {
            JwtService(JwtServiceJJWT(jwtConfig, CoreJsonProvider(instance())))
        }
        bind<AuthContextService>() with singleton {
            AuthContextServiceImpl(instance(), instance())
        }
    }

    override suspend fun onInit(vertx: Vertx, kodein: Kodein) {
        vertx.sharedData().getLocalMap<String, AuthShareableData>(AuthContants.LOCAL_DATA_AUTH_SERVICE_MAP)
                .put(AuthContants.LOCAL_DATA_AUTH_SERVICE_KEY, AuthShareableData(kodein.direct.instance<AuthContextService>()))
    }

    class AuthShareableData(val authContextService: AuthContextService): Shareable {}
}