package com.ufoscout.vertxk.kodein.router

import com.ufoscout.vertxk.kodein.VertxKVerticle

class TestWebController(val routerService: RouterService, val webExceptionService: WebExceptionService): VertxKVerticle() {

    override suspend fun start() {
        println("Start TestWebController")

        webExceptionService.registerTransformer<CustomTestException>({exp -> WebException(code = 12345, message = "CustomTestExceptionMessage") })

        val router = routerService.router()

        router.get("/core/test/fatal/:message").handler {
            var message = it.request().getParam("message")
            throw Exception(message)
        }

        router.get("/core/test/customException").handler {
            throw CustomTestException()
        }

        router.get("/core/test/webException/:code/:message").handler {
            var code = it.request().getParam("code")
            var message = it.request().getParam("message")
            throw WebException(message, code.toInt())
        }

        router.get("/core/test/slow").handler {
            Thread.sleep(2)
            println("Replying from: [${Thread.currentThread().name}]")
            it.response().end("ok")
        }
    }

}