package com.ufoscout.vertxk

class HttpClientKResponse<T>(
        val statusCode: Int,
        val body: T?,
        val cause: Throwable?
) {
}