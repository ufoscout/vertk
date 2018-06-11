package com.ufoscout.vertk.kodein

import com.ufoscout.vertk.K

interface VertxKComponent: K {

    suspend fun start()

}