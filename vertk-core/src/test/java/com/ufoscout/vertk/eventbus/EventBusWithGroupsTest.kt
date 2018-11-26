package com.ufoscout.vertk.eventbus;

import com.ufoscout.vertk.BaseTest
import com.ufoscout.vertk.eventBusWithGroups
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

class EventBusWithGroupsTest: BaseTest() {

    @Test
    fun shouldSendToSingleConsumer() = runBlocking<Unit> {

        val count = CountDownLatch(4);

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")

        val groupBusOne_groupOne = mutableListOf<String>()
        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupBusOne_groupOne.add(it.body())
            count.countDown()
        }

        groupBusOne.publish("hello1")
        groupBusOne.publish("hello2")
        vertx.eventBusWithGroups<String>("address_one").publish("hello3")
        groupBusOne.publish("hello4")
        vertx.eventBusWithGroups<String>("address_two").publish("hello5")

        count.await(50, TimeUnit.SECONDS)

        assertEquals(4, groupBusOne_groupOne.size)
        assertTrue(groupBusOne_groupOne.contains("hello1"))
        assertTrue(groupBusOne_groupOne.contains("hello2"))
        assertTrue(groupBusOne_groupOne.contains("hello3"))
        assertTrue(groupBusOne_groupOne.contains("hello4"))
    }

    @Test
    fun shouldSendFromAnotherBusWithSameAddress() = runBlocking<Unit> {

        val count = CountDownLatch(4);

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")

        val groupBusOne_groupOne = mutableListOf<String>()
        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupBusOne_groupOne.add(it.body())
            count.countDown()
        }

        val groupBusTwo = vertx.eventBusWithGroups<String>("address_one")

        groupBusTwo.publish("hello1")
        groupBusTwo.publish("hello2")
        groupBusOne.publish("hello3")
        groupBusTwo.publish("hello4")

        count.await(50, TimeUnit.SECONDS)

        assertEquals(4, groupBusOne_groupOne.size)
        assertTrue(groupBusOne_groupOne.contains("hello1"))
        assertTrue(groupBusOne_groupOne.contains("hello2"))
        assertTrue(groupBusOne_groupOne.contains("hello3"))
        assertTrue(groupBusOne_groupOne.contains("hello4"))
    }

    @Test
    fun shouldSendOnlyToSameAddress() = runBlocking<Unit> {

        val count = CountDownLatch(5);

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")

        val groupBusOne_groupOne = mutableListOf<String>()
        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupBusOne_groupOne.add(it.body())
            count.countDown()
        }

        val groupBusTwo_groupOne = mutableListOf<String>()
        val groupBusTwo = vertx.eventBusWithGroups<String>("address_two")
        groupBusTwo.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupBusTwo_groupOne.add(it.body())
            count.countDown()
        }

        groupBusOne.publish("hello1")
        groupBusTwo.publish("hello2")
        groupBusOne.publish("hello3")
        groupBusTwo.publish("hello4")
        groupBusTwo.publish("hello5")

        count.await(50, TimeUnit.SECONDS)

        assertEquals(2, groupBusOne_groupOne.size)
        assertTrue(groupBusOne_groupOne.contains("hello1"))
        assertTrue(groupBusOne_groupOne.contains("hello3"))

        assertEquals(3, groupBusTwo_groupOne.size)
        assertTrue(groupBusTwo_groupOne.contains("hello2"))
        assertTrue(groupBusTwo_groupOne.contains("hello4"))
        assertTrue(groupBusTwo_groupOne.contains("hello5"))
    }

    @Test
    fun shouldSendToOnlyOneConsumerPerGroup() = runBlocking<Unit> {

        val iterations = 100
        val count = CountDownLatch(2*iterations);

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")
        val groupOne = mutableListOf<String>()

        val groupBusTwo = vertx.eventBusWithGroups<String>("address_one")
        val groupTwo = mutableListOf<String>()

        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupOne.add(it.body())
            count.countDown()
        }

        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupOne.add(it.body())
            count.countDown()
        }

        groupBusOne.consumer("groupTwo") {
            println("groupBusOne_groupTwo called")
            groupTwo.add(it.body())
            count.countDown()
        }

        groupBusTwo.consumer("groupTwo") {
            println("groupBusTwo_groupTwo called")
            groupTwo.add(it.body())
            count.countDown()
        }

        groupBusTwo.consumer("groupOne") {
            println("groupBusTwo_groupOne called")
            groupOne.add(it.body())
            count.countDown()
        }

        for (i in 0 until iterations) {
            groupBusOne.publish("hello")
        }

        count.await(50, TimeUnit.SECONDS)

        assertEquals(iterations, groupOne.size)
        assertEquals(iterations, groupTwo.size)
    }

    @Test
    fun shouldSendToDifferentGroupsToASingleConsumer() = runBlocking<Unit> {

        val count = CountDownLatch(8);

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")

        val groupBusOne_groupOne = AtomicInteger(0)
        groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupBusOne_groupOne.incrementAndGet()
            count.countDown()
        }

        val groupBusOne_groupTwo = AtomicInteger(0)
        groupBusOne.awaitConsumer("groupTwo") {
            println("groupBusOne_groupTwo called")
            groupBusOne_groupTwo.incrementAndGet()
            count.countDown()
        }

        val groupBusTwo_groupOne = AtomicInteger(0)
        val groupBusTwo = vertx.eventBusWithGroups<String>("address_one")
        groupBusTwo.consumer("groupOne") {
            println("groupBusTwo_groupOne called")
            groupBusOne_groupOne.incrementAndGet()
            count.countDown()
        }

        groupBusOne.publish("hello")
        groupBusTwo.publish("hello")
        groupBusTwo.publish("hello")
        groupBusTwo.publish("hello")

        count.await(50, TimeUnit.SECONDS)

        assertEquals(4, groupBusOne_groupOne.get() + groupBusTwo_groupOne.get())
        assertEquals(4, groupBusOne_groupTwo.get())
    }

    @Test
    fun shouldRemoveTheConsumer() = runBlocking<Unit> {

        val iterations = 100
        val count = AtomicReference(CountDownLatch(iterations))

        val groupBusOne = vertx.eventBusWithGroups<String>("address_one")
        val groupOne = AtomicInteger()

        val groupBusTwo = vertx.eventBusWithGroups<String>("address_one")
        val groupTwo = AtomicInteger()

        val messageConsumerOne = groupBusOne.consumer("groupOne") {
            println("groupBusOne_groupOne called")
            groupOne.getAndIncrement()
            count.get().countDown()
        }

        groupBusTwo.consumer("groupOne") {
            println("groupBusTwo_groupOne called")
            groupTwo.getAndIncrement()
            count.get().countDown()
        }

        for (i in 0 until iterations) {
            groupBusOne.publish("hello")
        }

        count.get().await(50, TimeUnit.SECONDS)
        assertEquals(100, groupOne.get() + groupTwo.get())

        // Now let's remove one consumer
        println("remove consumer")
        groupOne.set(0)
        groupTwo.set(0)
        count.set(CountDownLatch(iterations))
        messageConsumerOne.unregister()

        for (i in 0 until iterations) {
            groupBusOne.publish("hello")
        }

        count.get().await(50, TimeUnit.SECONDS)
        assertEquals(0, groupOne.get())
        assertEquals(100, groupTwo.get())

    }

}