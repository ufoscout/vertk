package com.weweb.core.service

import com.ufoscout.vertxk.kodein.VertxKComponent
import io.vertx.ext.web.Router

interface RouterService: VertxKComponent {

    fun router(): Router

}