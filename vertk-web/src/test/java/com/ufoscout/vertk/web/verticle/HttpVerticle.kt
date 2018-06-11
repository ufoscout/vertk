package com.ufoscout.vertk.web.verticle

import com.ufoscout.vertk.web.BaseTest
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*

class HttpVerticle: CoroutineVerticle() {

    companion object {
        val path = "/" + UUID.randomUUID().toString()
    }

    override suspend fun start() {

        val router = BaseTest.router

        router.restDelete(path) {
            ResponseDTO(it.request().method().toString())
        }


        router.restGet(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.restOptions(path) {
            ResponseDTO(it.request().method().toString())
        }

        router.restPatch<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.restPost<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.restPut<RequestDTO>(path) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }
    }

}