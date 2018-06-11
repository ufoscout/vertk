package com.ufoscout.vertk.verticle

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.K
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.util.*

class HttpVerticle: CoroutineVerticle(), K {

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

        router.restPatch(path, RequestDTO::class) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.restPost(path, RequestDTO::class) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }

        router.restPut(path, RequestDTO::class) { rc, body ->
            ResponseDTO("${rc.request().method()}-${body.message}")
        }
    }

}