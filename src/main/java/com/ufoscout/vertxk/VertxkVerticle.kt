package com.ufoscout.vertxk

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

abstract class VertxkVerticle : AbstractVerticle() {

    override final fun start(startFuture: Future<Void>) {
        runVertxCoroutine(vertx) {
            try {
                start()
                vertxkStart()
                startFuture.complete()
            } catch (t: Throwable) {
                startFuture.fail(t)
            }
        }
    }

    override final fun stop(stopFuture: Future<Void>) {
        runVertxCoroutine(vertx) {
            try {
                stop()
                vertxkStop()
                stopFuture.complete()
            } catch (t: Throwable) {
                stopFuture.fail(t)
            }
        }
    }

    override final fun start() {}

    override final fun stop() {}

    open suspend fun vertxkStart() {}

    open suspend fun vertxkStop() {}

}