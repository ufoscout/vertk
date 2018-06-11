package com.ufoscout.vertk

import io.vertx.core.Handler
import io.vertx.core.http.HttpServerRequest
import io.vertx.kotlin.coroutines.awaitResult

class HttpServer(val httpServer: io.vertx.core.http.HttpServer) {

    fun actualPort(): Int {
        return httpServer.actualPort()
    }

    fun requestHandler(handler: Handler<HttpServerRequest>): HttpServer {
        httpServer.requestHandler(handler)
        return this
    }

    suspend fun listen(port: Int, host: String = "127.0.0.1"): HttpServer {
        awaitResult<io.vertx.core.http.HttpServer> { wait ->
            httpServer.listen(port, host, wait)
        }
        return this
    }

}