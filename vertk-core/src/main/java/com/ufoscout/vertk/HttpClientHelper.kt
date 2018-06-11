package com.ufoscout.vertk

import io.vertx.core.http.HttpClientRequest
import io.vertx.core.json.JsonObject
import io.vertx.core.Handler
import io.vertx.core.http.HttpClientResponse
import kotlin.reflect.KClass


internal object HttpClientHelper {

    fun <T: Any> handlerRestResponse(response: HttpClientResponse, responseClass: KClass<T>, handler: Handler<com.ufoscout.vertk.HttpClientResponse<T>>) {
        response.bodyHandler({ body ->
            try {
                handler.handle(HttpClientResponse(response.statusCode(), body.toJsonObject().mapTo(responseClass.javaObjectType), null))
            } catch (e: Throwable) {
                handler.handle(HttpClientResponse(response.statusCode(), null, e))
            }
        })
    }

    fun <T : Any> restRequest(request: HttpClientRequest, handler: Handler<com.ufoscout.vertk.HttpClientResponse<T>>, vararg headers: Pair<String, String>) {
        try {
            headers.forEach { request.putHeader(it.first, it.second) }
            request.end()
        } catch (e: Throwable) {
            handler.handle(HttpClientResponse(0, null, e))
        }
    }

    fun <T : Any> restRequestWithBody(request: HttpClientRequest, body: Any, handler: Handler<com.ufoscout.vertk.HttpClientResponse<T>>, vararg headers: Pair<String, String>) {
        try {
            request.putHeader("content-type", "application/json")
            headers.forEach { request.putHeader(it.first, it.second) }
            request.setChunked(true)
            request.write(JsonObject.mapFrom(body).encode())
            request.end()
        } catch (e: Throwable) {
            handler.handle(HttpClientResponse(0, null, e))
        }
    }

}