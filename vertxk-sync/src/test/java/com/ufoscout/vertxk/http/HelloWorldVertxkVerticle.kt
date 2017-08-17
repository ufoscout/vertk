package com.ufoscout.vertxk.http

import com.ufoscout.vertxk.VertxkVerticle
import com.ufoscout.vertxk.awaitResult
import io.vertx.core.http.HttpServer

class HelloWorldVertxkVerticle : VertxkVerticle() {

    override suspend fun vertxkStart() {
        val server = awaitResult<HttpServer> {
            vertx.createHttpServer()
                    .requestHandler { r -> r.response().end("Hello, World!") }
                    .listen(0, it)
        }
        port = server.actualPort()
    }

    companion object {
        var port: Int = 0;
    }
}