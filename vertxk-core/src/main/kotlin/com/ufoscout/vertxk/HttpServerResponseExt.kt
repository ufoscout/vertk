package com.ufoscout.vertxk

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

interface HttpServerResponseExt {

    fun HttpServerResponse.endWithJson(obj: Any) {
        this.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(obj))
    }

}