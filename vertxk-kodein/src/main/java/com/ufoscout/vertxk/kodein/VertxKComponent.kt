package com.ufoscout.vertxk.kodein

import com.ufoscout.vertxk.K

interface VertxKComponent: K {

    suspend fun start()

}