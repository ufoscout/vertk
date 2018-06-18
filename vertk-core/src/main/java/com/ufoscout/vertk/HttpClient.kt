package com.ufoscout.vertk

import io.vertx.core.http.HttpClientOptions
import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.coroutines.awaitEvent
import kotlin.reflect.KClass

class HttpClient(val vertk: Vertk, val httpClientOptions: HttpClientOptions) {

    val client = vertk.vertx().createHttpClient(httpClientOptions)

    suspend inline fun <reified T : Any> restDelete(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.delete(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend inline fun <reified T : Any> restGet(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.get(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend inline fun <reified T : Any> restOptions(port: Int, host: String, requestUri: String, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.options(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend inline fun <reified T : Any> restPatch(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.request(HttpMethod.PATCH, port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

    suspend inline fun <reified T : Any> restPost(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.post(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

    suspend inline fun <reified T : Any> restPut(port: Int, host: String, requestUri: String, body: Any, vararg headers: Pair<String, String>): HttpClientResponse<T> {
        return awaitEvent<HttpClientResponse<T>> {
            val request = client.put(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse<T>(response, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

}
