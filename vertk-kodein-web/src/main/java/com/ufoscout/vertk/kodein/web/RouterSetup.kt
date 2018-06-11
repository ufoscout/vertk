package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.VertkKodeinStartable

class RouterSetup(val webExceptionService: WebExceptionService): VertkKodeinStartable {

    override suspend fun start() {
        webExceptionService.registerTransformer<BadRequestException>({ ex -> WebException(code = 400, message = ex.message!!) })
    }

}
