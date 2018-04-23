package com.ufoscout.vertxk.kodein.stub

import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.util.*

object StubModule {

    val RANDOM_NAME= UUID.randomUUID().toString()

    fun module(): Kodein.Module {
        return Kodein.Module{
            bind<String>() with singleton { RANDOM_NAME }
            bind<VertxKComponentImpl>() with singleton { VertxKComponentImpl(instance()) }
        }
    }

}