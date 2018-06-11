package com.ufoscout.vertk.kodein.stub

import com.ufoscout.vertk.kodein.VertxKVerticle
import java.util.concurrent.atomic.AtomicInteger

class VertxKVerticleImpl(val name: String) : VertxKVerticle() {

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