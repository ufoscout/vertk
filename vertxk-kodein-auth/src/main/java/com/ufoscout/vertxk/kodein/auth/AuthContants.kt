package com.ufoscout.vertxk.kodein.auth

interface AuthContants {

    companion object {

        val JWT_TOKEN_HEADER = "Authorization"
        val JWT_TOKEN_HEADER_SUFFIX = "Bearer "

        val LOCAL_DATA_AUTH_SERVICE_MAP = "vertxk_localDataAuthServiceMap"
        val LOCAL_DATA_AUTH_SERVICE_KEY = "vertxk_localDataAuthServiceKey"

    }

}
