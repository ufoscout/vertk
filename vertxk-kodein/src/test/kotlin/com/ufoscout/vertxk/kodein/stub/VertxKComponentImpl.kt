package com.ufoscout.vertxk.kodein.stub

import com.ufoscout.vertxk.kodein.VertxKComponent
import kotlinx.coroutines.experimental.runBlocking
import java.util.concurrent.atomic.AtomicInteger

class VertxKComponentImpl(val name: String) : VertxKComponent {

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