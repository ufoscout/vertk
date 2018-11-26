package com.ufoscout.vertk.kodein.web

import com.ufoscout.coreutils.validation.SimpleValidatorService
import com.ufoscout.coreutils.validation.ValidationException
import com.ufoscout.vertk.web.awaitRestPost
import io.vertx.kotlin.coroutines.CoroutineVerticle
import java.lang.RuntimeException

class TestWebController(val routerService: RouterService, val webExceptionService: WebExceptionService): CoroutineVerticle() {

    override suspend fun start() {
        println("Start TestWebController")

        webExceptionService.registerTransformer<CustomTestException> {ex -> WebException(cause = ex, code = 12345, message = "CustomTestExceptionMessage") }

        val router = routerService.router()

        router.get("/core/test/fatal/:message").handler {
            var message = it.request().getParam("message")
            throw Exception(message)
        }

        router.get("/core/test/customException").handler {
            throw CustomTestException()
        }

        router.get("/core/test/badRequestException/:message").handler {
            var message = it.request().getParam("message")
            throw BadRequestException(message)
        }

        router.get("/core/test/webException/:code/:message").handler {
            var code = it.request().getParam("code")
            var message = it.request().getParam("message")
            throw WebException(RuntimeException(), message, code.toInt())
        }

        router.get("/core/test/slow").handler {
            Thread.sleep(2)
            println("Replying from: [${Thread.currentThread().name}]")
            it.response().end("ok")
        }

        router.awaitRestPost<BeanToValidate>("/core/test/validationException", { rc, bean ->
            SimpleValidatorService().validator<BeanToValidate>()
                    .add("id", "id should not be null", {it.id!=null})
                    .add("name", "name should not be null", {it.name!=null})
                    .build()
                    .validateThrowException(bean)
        })

    }

}