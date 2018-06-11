package com.ufoscout.vertk

class HttpClientKResponse<T>(
        val statusCode: Int,
        val body: T?,
        val cause: Throwable?
) {
}