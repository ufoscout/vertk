package com.ufoscout.vertxk.http

import com.ufoscout.vertxk.VertxkVerticle
import com.ufoscout.vertxk.awaitResult
import io.vertx.core.http.HttpServer

class HelloWorldVertxkVerticle : VertxkVerticle() {

    override suspend fun vertxkStart() {
        awaitResult<HttpServer> {
            vertx.createHttpServer()
                    .requestHandler { r -> r.response().end("Hello, World!") }
                    .listen(8080, it)
        }
    }
}