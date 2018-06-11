package com.ufoscout.vertk

class HttpClientResponse<T>(
        val statusCode: Int,
        val body: T?,
        val cause: Throwable?
) {
}