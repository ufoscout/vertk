package io.vertx.ext.coroutine

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class HelloWorldVerticle : AbstractVerticle() {

    override fun start(fut: Future<Void>) {
        vertx
                .createHttpServer()
                .requestHandler { r -> r.response().end("Hello, World!") }
                .listen(8080) { result ->
                    if (result.succeeded()) {
                        fut.complete()
                    } else {
                        fut.fail(result.cause())
                    }
                }
    }
}