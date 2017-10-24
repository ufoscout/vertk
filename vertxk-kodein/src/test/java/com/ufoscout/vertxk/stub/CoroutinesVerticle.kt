package com.ufoscout.vertxk.stub

import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.experimental.runBlocking

class CoroutinesVerticle(val name: String) : CoroutineVerticle() {

    override suspend fun start() {
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