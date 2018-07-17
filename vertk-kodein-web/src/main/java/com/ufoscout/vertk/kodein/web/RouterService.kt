package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinStartable
import io.vertx.ext.web.Router

interface RouterService: VertkKodeinStartable {

    fun router(): Router

}