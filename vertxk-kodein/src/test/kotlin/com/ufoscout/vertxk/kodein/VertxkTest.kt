package com.ufoscout.vertxk.kodein

import com.ufoscout.vertxk.BaseTest
import com.ufoscout.vertxk.kodein.stub.VertxKComponentImpl
import com.ufoscout.vertxk.kodein.stub.StubModule
import io.vertx.core.DeploymentOptions
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class VertxkTest: BaseTest() {

    @Before
    fun setUp() {
        VertxKComponentImpl.RESET();
    }

    @Test
    fun shouldDeploy() = runBlocking<Unit> {

        VertxK.start(vertx = vertx, modules = StubModule.module())
        assertTrue(VertxKComponentImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKComponentImpl.NAME)
        assertEquals(1, VertxKComponentImpl.COUNT.get())

    }

    @Test
    fun shouldDeployWithDeploymentOptions() = runBlocking<Unit> {

        val instances = Random().nextInt(10) + 1
        val deploymentOptions = DeploymentOptions().setInstances(instances)


        VertxK.start(vertx = vertx, deploymentOptions = deploymentOptions, modules = StubModule.module())
        assertTrue(VertxKComponentImpl.STARTED)
        assertEquals(StubModule.RANDOM_NAME, VertxKComponentImpl.NAME)
        assertEquals(instances, VertxKComponentImpl.COUNT.get())

    }
}