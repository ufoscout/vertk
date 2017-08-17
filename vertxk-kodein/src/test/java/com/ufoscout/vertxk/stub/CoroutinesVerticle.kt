package com.ufoscout.vertxk.stub

import com.ufoscout.vertxk.VertxkVerticle
import kotlinx.coroutines.experimental.runBlocking

class CoroutinesVerticle(val name: String) : VertxkVerticle() {

    override suspend fun vertxkStart() {
        runBlocking {
            STARTED = true
            NAME = name
        }
    }

    companion object {
        var STARTED = false
        var NAME = ""
    }

}