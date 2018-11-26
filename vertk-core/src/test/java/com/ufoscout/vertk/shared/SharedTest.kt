package com.ufoscout.vertk.shared

import com.ufoscout.vertk.BaseTest
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class SharedTest: BaseTest() {

    @Test
    fun shouldGetSharedData() = runBlocking<Unit> {

        val map_name = UUID.randomUUID().toString()

        val map = vertx.sharedData().awaitGetAsyncMap<String, String>(map_name)
        assertNotNull(map)

        val key = UUID.randomUUID().toString()
        assertNull(map.awaitGet(key))

        map.awaitPut(key, key+key)
        assertEquals(key+key, map.awaitGet(key))
    }

    @Test
    fun shouldGetOrCompute() = runBlocking<Unit> {

        val map_name = UUID.randomUUID().toString()
        val map = vertx.sharedData().awaitGetAsyncMap<String, String>(map_name)

        val key = UUID.randomUUID().toString()
        val atomicInteger = AtomicInteger(0)

        assertEquals( key+key+key, map.awaitGetOrCompute(key, {k -> atomicInteger.getAndIncrement(); k+k+k}))
        assertEquals(1, atomicInteger.get())

        assertEquals( key+key+key, map.awaitGetOrCompute(key, {k -> atomicInteger.getAndIncrement(); k+k+k}))
        assertEquals(1, atomicInteger.get())

        map.awaitClear()

        assertEquals( key+key+key, map.awaitGetOrCompute(key, {k -> atomicInteger.getAndIncrement(); k+k+k}))
        assertEquals(2, atomicInteger.get())
    }
}