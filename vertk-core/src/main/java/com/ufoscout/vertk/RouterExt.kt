package com.ufoscout.vertk

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
import kotlin.reflect.KClass

interface RouterExt {

    fun Router.restDelete(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(delete(path), handler)
    }

    fun Router.restDeleteWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(deleteWithRegex(regex), handler)
    }

    fun Router.restGet(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(get(path), handler)
    }

    fun Router.restGetWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(getWithRegex(regex), handler)
    }

    fun Router.restHeadWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(headWithRegex(regex), handler)
    }

    fun Router.restOptions(path: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(options(path), handler)
    }

    fun Router.restOptionsWithRegex(regex: String, handler: suspend (rc: RoutingContext) -> Any) {
        rest(optionsWithRegex(regex), handler)
    }

    fun <I : Any> Router.restPatch(path: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(patch(path), kClass, handler)
    }

    fun <I : Any> Router.restPatchWithRegex(regex: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(patchWithRegex(regex), kClass, handler)
    }

    fun <I : Any> Router.restPost(path: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(post(path), kClass, handler)
    }

    fun <I : Any> Router.restPostWithRegex(regex: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(postWithRegex(regex), kClass, handler)
    }

    fun <I : Any> Router.restPut(path: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(put(path), kClass, handler)
    }

    fun <I : Any> Router.restPutWithRegex(regex: String, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        restWithBody(putWithRegex(regex), kClass, handler)
    }

    fun Router.rest(route: Route, handler: suspend (rc: RoutingContext) -> Any) {
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

    fun <I : Any> Router.restWithBody(route: Route, kClass: KClass<I>, handler: suspend (rc: RoutingContext, body: I) -> Any) {
        route
                .consumes("application/json")
                .produces("application/json")
                .handler({ rc ->
                    launch(Vertx.currentContext().dispatcher()) {
                        try {
                            val bodyBuffer = awaitEvent<Buffer> {
                                rc.request().bodyHandler(it)
                            }
                            val body = bodyBuffer.toJsonObject().mapTo(kClass.javaObjectType)
                            val result = handler(rc, body)
                            rc.response().putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(result))
                        } catch (e: Exception) {
                            rc.fail(e)
                        }
                    }
                })
    }

}
