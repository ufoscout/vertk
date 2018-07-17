package com.ufoscout.vertk.web.verticle

import com.ufoscout.vertk.web.BaseTest
import com.ufoscout.vertk.web.*
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*

class HttpVerticle: CoroutineVerticle() {

    companion object {
        val path = "/" + UUID.randomUUID().toString()
    }

    override suspend fun start() {

        val router = BaseTest.router

        router.awaitRestDelete(path) {
            ResponseDTO(it.request().method().toString())
        }


        router.awaitRestGet(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.awaitRestOptions(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.awaitRestPatch<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.awaitRestPost<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.awaitRestPut<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }
    }

}