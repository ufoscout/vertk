package com.ufoscout.vertk.web

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.awaitEvent
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.experimental.launch
import java.lang.Exception


fun Router.awaitRestDelete(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.delete(path), handler)
}

fun Router.awaitRestDeleteWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.deleteWithRegex(regex), handler)
}

fun Router.awaitRestGet(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.get(path), handler)
}

fun Router.awaitRestGetWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.getWithRegex(regex), handler)
}

fun Router.awaitRestHeadWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.headWithRegex(regex), handler)
}

fun Router.awaitRestOptions(path: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.options(path), handler)
}

fun Router.awaitRestOptionsWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
    rest(this.optionsWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.awaitRestPatch(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.patch(path), handler)
}

inline fun <reified I : Any> Router.awaitRestPatchWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.patchWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.awaitRestPost(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.post(path), handler)
}

inline fun <reified I : Any> Router.awaitRestPostWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.postWithRegex(regex), handler)
}

inline fun <reified I : Any> Router.awaitRestPut(path: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.put(path), handler)
}

inline fun <reified I : Any> Router.awaitRestPutWithRegex(regex: String, noinline handler: suspend (rc: RoutingContext, body: I) -> Any) {
    restWithBody<I>(this.putWithRegex(regex), handler)
}

fun rest(route: Route, handler: suspend (rc: RoutingContext) -> Any) {
    route
            .produces("application/json")
            .handler({
                launch(Vertx.currentContext().dispatcher()) {
                    try {
                        val result = handler(it)
                        var resultJson = if (result is String) {
                            result
                        } else {
                            Json.encode(result)
                        }
                        it.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson)
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
                        var resultJson = if (result is String) {
                            result
                        } else {
                            Json.encode(result)
                        }
                        rc.response().putHeader("Content-Type", "application/json; charset=utf-8").end(resultJson)
                    } catch (e: Exception) {
                        rc.fail(e)
                    }
                }
            })
}


