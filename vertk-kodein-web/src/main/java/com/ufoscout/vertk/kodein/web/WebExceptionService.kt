package com.ufoscout.vertk.kodein.web

import kotlin.reflect.KClass

interface WebExceptionService {

    fun <T : Throwable> registerTransformer(klass: KClass<T>, map: (exc: T) -> WebException)

    fun get(ex: Throwable?): WebException?

}

inline fun <reified T : Throwable> WebExceptionService.registerTransformer(noinline map: (exc: T?) -> WebException) = registerTransformer(T::class, map)
