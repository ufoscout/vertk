package com.ufoscout.vertk.kodein.router

import com.ufoscout.vertk.kodein.VertxKComponent
import io.vertx.ext.web.Router

interface RouterService: VertxKComponent {

    fun router(): Router

}