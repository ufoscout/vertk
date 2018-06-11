package com.ufoscout.vertk.kodein.router

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

class WebExceptionServiceImpl : WebExceptionService {

    private val exceptions = ConcurrentHashMap<KClass<Throwable>, (Throwable) -> WebException>()

    override fun <T : Throwable> registerTransformer(klass: KClass<T>, map: (exc: T) -> WebException) {
        exceptions.put(klass as KClass<Throwable>, map as (Throwable) -> WebException);
    }

    override fun get(ex: Throwable?): WebException? {
        if (ex!=null) {
            val mapper = exceptions[ex::class]
            if (mapper != null) {
                return mapper(ex)
            }
        }
        return null
    }


}