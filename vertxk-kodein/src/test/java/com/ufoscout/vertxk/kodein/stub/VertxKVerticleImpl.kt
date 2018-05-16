package com.ufoscout.vertxk.kodein.stub

import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.concurrent.atomic.AtomicInteger

class VertxKVerticleImpl(val name: String) : CoroutineVerticle() {

    override suspend fun start() {
        STARTED = true
        NAME = name
        COUNT.getAndIncrement()
    }

    companion object {
        var STARTED = false
        var NAME = ""
        var COUNT = AtomicInteger(0)

        fun RESET() {
            STARTED = false
            NAME = ""
            COUNT = AtomicInteger(0)
        }
    }

}