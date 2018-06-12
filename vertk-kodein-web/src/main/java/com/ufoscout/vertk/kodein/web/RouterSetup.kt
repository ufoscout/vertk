package com.ufoscout.vertk.kodein.web

import com.ufoscout.coreutils.validation.ValidationException
import com.ufoscout.vertk.kodein.VertkKodeinStartable

class RouterSetup(val webExceptionService: WebExceptionService): VertkKodeinStartable {

    override suspend fun start() {
        webExceptionService.registerTransformer<BadRequestException>({ ex -> WebException(code = 400, message = ex.message!!) })
        webExceptionService.registerTransformer<ValidationException>({ ex -> WebException(code = 422, message = ex.message!!, details = ex.violations) })
    }

}
