package com.ufoscout.vertk.kodein.auth

interface AuthContants {

    companion object {

        val JWT_TOKEN_HEADER = "Authorization"
        val JWT_TOKEN_HEADER_SUFFIX = "Bearer "

        val LOCAL_DATA_AUTH_SERVICE_MAP = "vertk_localDataAuthServiceMap"
        val LOCAL_DATA_AUTH_SERVICE_KEY = "vertk_localDataAuthServiceKey"

    }

}
