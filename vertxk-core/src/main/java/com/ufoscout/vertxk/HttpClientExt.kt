package com.ufoscout.vertxk

import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpMethod
import io.vertx.kotlin.coroutines.awaitEvent
import kotlin.reflect.KClass

interface HttpClientExt {

    suspend fun <T : Any> HttpClient.restDelete(port: Int, host: String, requestUri: String, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = delete(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend fun <T : Any> HttpClient.restGet(port: Int, host: String, requestUri: String, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = get(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend fun <T : Any> HttpClient.restOptions(port: Int, host: String, requestUri: String, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = options(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequest(request, it, *headers)
        }
    }

    suspend fun <T : Any> HttpClient.restPatch(port: Int, host: String, requestUri: String, body: Any, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = request(HttpMethod.PATCH, port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

    suspend fun <T : Any> HttpClient.restPost(port: Int, host: String, requestUri: String, body: Any, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = post(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

    suspend fun <T : Any> HttpClient.restPut(port: Int, host: String, requestUri: String, body: Any, responseClass: KClass<T>, vararg headers: Pair<String, String>): HttpClientKResponse<T> {
        return awaitEvent<HttpClientKResponse<T>> {
            val request = put(port, host, requestUri) { response -> HttpClientHelper.handlerRestResponse(response, responseClass, it) }
            HttpClientHelper.restRequestWithBody(request, body, it, *headers)
        }
    }

}
