package com.ufoscout.vertk.web.client

import io.vertx.core.MultiMap
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.core.streams.ReadStream
import io.vertx.ext.web.client.HttpRequest
import io.vertx.ext.web.client.HttpResponse
import io.vertx.kotlin.coroutines.awaitResult

fun <T : Any> HttpRequest<T>.putHeaders(vararg headers: Pair<String, String>): HttpRequest<T> {
    headers.forEach { this.putHeader(it.first, it.second) }
    return this
}

suspend fun <T : Any> HttpRequest<T>.awaitSend(): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.send(it)
    }
}

suspend fun <T : Any> HttpRequest<T>.awaitSendJson(body: Any): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.sendJson(body, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.awaitSendBuffer(body: Buffer): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.sendBuffer(body, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.awaitSendForm(body: MultiMap): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.sendForm(body, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.awaitSendJsonObject(body: JsonObject): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.sendJsonObject(body, it)
    }
}

suspend fun <T : Any> HttpRequest<T>.awaitSendStream(body: ReadStream<Buffer>): HttpResponse<T> {
    return awaitResult<HttpResponse<T>> {
        this.sendStream(body, it)
    }
}

inline fun <reified T : Any> HttpResponse<out Any>.bodyAsJson(): T {
        return this.bodyAsJson(T::class.java)
}
