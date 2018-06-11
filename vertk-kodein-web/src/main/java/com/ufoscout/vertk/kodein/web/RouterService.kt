package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinStartable
import com.ufoscout.vertk.web.Router

interface RouterService: VertkKodeinStartable {

    fun router(): Router

}