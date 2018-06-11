package com.ufoscout.vertk.kodein.router

import com.ufoscout.vertk.BaseTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class WebExceptionServiceTest : BaseTest() {

    val webEx: WebExceptionService = WebExceptionServiceImpl()

    @Test
    fun shouldReturnNullIfMissingMapper() {
        assertNull(webEx.get(RuntimeException()))
    }

    @Test
    fun shouldApplyTheCorrectTransformer() {
        webEx.registerTransformer<ExceptionOne>({ex -> WebException(code = 1) })
        webEx.registerTransformer<ExceptionTwo>({ex -> WebException(code = 2) })

        assertNull(webEx.get(RuntimeException()))
        assertEquals(1, webEx.get(ExceptionOne())!!.statusCode())
        assertEquals(2, webEx.get(ExceptionTwo())!!.statusCode())

    }


    class ExceptionOne: RuntimeException() {}

    class ExceptionTwo: RuntimeException() {}

}