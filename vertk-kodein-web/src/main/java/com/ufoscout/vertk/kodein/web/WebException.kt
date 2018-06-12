package com.ufoscout.vertk.kodein.web

import io.netty.handler.codec.http.HttpResponseStatus

class WebException : RuntimeException {

    companion object {
        val DEFAULT_DETAILS: Map<String, List<String>> = mapOf()
    }
    private val statusCode: Int
    private val details: Map<String, List<String>>

    constructor(message: String = "", code: Int = HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), details: Map<String, List<String>> = DEFAULT_DETAILS) : super(message) {
        statusCode = code
        this.details = details
    }

    constructor(cause: Throwable, message: String = "", code: Int = HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), details: Map<String, List<String>> = DEFAULT_DETAILS) : super(message, cause) {
        statusCode = code
        this.details = details
    }

    public fun statusCode(): Int = statusCode
    public fun details() = details
    
}
