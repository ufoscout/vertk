package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.auth.AuthServiceImpl
import com.ufoscout.coreutils.jwt.JwtConfig
import com.ufoscout.coreutils.jwt.JwtServiceJJWT
import com.ufoscout.coreutils.jwt.kotlin.CoreJsonProvider
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.VertkKodeinModule
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AuthModule(val jwtConfig: JwtConfig): VertkKodeinModule {

    override fun module() = Kodein.Module {
        bind<AuthSetup>() with singleton { AuthSetup(instance()) }
        bind<AuthService>() with singleton {
            AuthServiceImpl(instance())
        }
        bind<AuthContextService>() with singleton {
            AuthContextServiceImpl(instance(), instance())
        }
        bind<JwtService>() with singleton {
            JwtService(JwtServiceJJWT(jwtConfig, CoreJsonProvider(instance())))
        }
    }

    override suspend fun onInit(vertk: Vertk, kodein: Kodein) {
    }

}