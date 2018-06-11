package com.ufoscout.vertk.kodein.web

import io.netty.handler.codec.http.HttpResponseStatus

class WebException : RuntimeException {

    private val statusCode: Int

    constructor(message: String = "", code: Int = HttpResponseStatus.INTERNAL_SERVER_ERROR.code()) : super(message) {
        statusCode = code
    }

    constructor(message: String = "", cause: Throwable, code: Int = HttpResponseStatus.INTERNAL_SERVER_ERROR.code()) : super(message, cause) {
        statusCode = code
    }

    public fun statusCode(): Int = statusCode
    
}
