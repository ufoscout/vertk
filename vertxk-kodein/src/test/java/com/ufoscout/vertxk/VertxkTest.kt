package com.ufoscout.vertxk

import com.ufoscout.vertxk.stub.CoroutinesVerticle
import com.ufoscout.vertxk.stub.StubModule
import com.ufoscout.vertxk.util.VertxkKodein
import io.vertx.kotlin.coroutines.awaitResult
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import org.kodein.di.jxinject.jxInjectorModule
import java.util.*

class VertxkTest: BaseTest() {

    @Test
    fun shouldDeploy() = runBlocking<Unit> {

        StubModule.ON_INIT_CALLED = false
        StubModule.MODULE_CALLED = false
        CoroutinesVerticle.STARTED = false;

        Vertxk.start(vertx, StubModule())
        Assert.assertTrue(StubModule.ON_INIT_CALLED)
        Assert.assertTrue(StubModule.MODULE_CALLED)
        Assert.assertTrue(CoroutinesVerticle.STARTED)
        Assert.assertEquals(StubModule.COROUTINE_VERTICLE_NAME, CoroutinesVerticle.NAME)
    }

}