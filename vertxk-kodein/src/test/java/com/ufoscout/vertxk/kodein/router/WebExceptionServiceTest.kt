package com.ufoscout.vertxk.kodein.router

import com.ufoscout.vertxk.BaseTest
import junit.framework.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Test

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