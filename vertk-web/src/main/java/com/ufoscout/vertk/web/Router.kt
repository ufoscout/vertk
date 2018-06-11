package com.ufoscout.vertk.web

import com.ufoscout.vertk.Vertk
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch
import java.lang.Exception

class Router(val vertk: Vertk) {

    companion object {
        fun router(vertk: Vertk) = Router(vertk)
    }

    val router = io.vertx.ext.web.Router.router(vertk.vertx())

    fun router() = router
    fun route() = router.route()

    fun accept(httpServerRequest: HttpServerRequest) {
        router.accept(httpServerRequest)
    }

    fun restDelete(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.delete(path), handler)
    }

    fun restDeleteWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.deleteWithRegex(regex), handler)
    }

    fun restGet(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.get(path), handler)
    }

    fun restGetWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.getWithRegex(regex), handler)
    }

    fun restHeadWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.headWithRegex(regex), handler)
    }

    fun restOptions(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.options(path), handler)
    }

    fun restOptionsWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(router.optionsWithRegex(regex), handler)
    }

    inline fun <reified I : Any> restPatch(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.patch(path), handler)
    }

    inline fun <reified I : Any> restPatchWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.patchWithRegex(regex), handler)
    }

    inline fun <reified I : Any> restPost(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.post(path), handler)
    }

    inline fun <reified I : Any> restPostWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.postWithRegex(regex), handler)
    }

    inline fun <reified I : Any> restPut(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.put(path), handler)
    }

    inline fun <reified I : Any> restPutWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody<I>(router.putWithRegex(regex), handler)
    }

    fun rest(route: Route, handler: suspend (rc: RoutingContext) -> Any) {
        route
                .produces("application/json")
                .handler({
                    launch(Vertx.currentContext().dispatcher()) {
                        try {
                            val result = handler(it)
                            it.response().putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(result))
                        } catch (e: Exception) {
                            it.fail(e)
                        }
                    }
                })
    }

    inline fun <reified I : Any> restWithBody(route: Route, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
        route
                .consumes("application/json")
                .produces("application/json")
                .handler({ rc ->
                    launch(Vertx.currentContext().dispatcher()) {
                        try {
                            val bodyBuffer = awaitEvent<Buffer> {
                                rc.request().bodyHandler(it)
                            }
                            val body = bodyBuffer.toJsonObject().mapTo(I::class.java)
                            val result = handler(rc, body)
                            rc.response().putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(result))
                        } catch (e: Exception) {
                            rc.fail(e)
                        }
                    }
                })
    }

}
