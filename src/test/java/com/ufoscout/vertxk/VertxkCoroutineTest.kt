package com.ufoscout.vertxk

import io.vertx.core.CompositeFuture
import io.vertx.core.Future
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpServer
import io.vertx.groovy.ext.unit.TestContext_GroovyExtension
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by ufo on 15/08/17.
 */
class VertxkCoroutineTest : BaseTest() {

    @Test
    fun shouldAwaitASingleEvent() = runBlocking<Unit> {
        val delay = 500L
        val start = System.currentTimeMillis();
        await<Long> { h -> vertx.setTimer(delay, h) }
        val executionTime = System.currentTimeMillis() - start
        println("This was print after $executionTime ms")
        Assert.assertTrue(executionTime >= delay)
    }

    @Test
    fun shouldReceiveAnEventReplySynchronously() = runBlocking<Unit> {

        val consumer = vertx.eventBus().consumer<String>("someAddressA")
        consumer.handler { message ->
            println("consumer receive message ${message.body()}")
            message.reply("received: [${message.body()}]")
        }
        //wait consumer to complete register synchronously
        awaitResult<Void> { consumer.completionHandler(it) }

        val id = UUID.randomUUID().toString()

        //send message and wait reply synchronously
        val reply = awaitResult<Message<String>> { h -> vertx.eventBus().send("someAddressA", id, h) }
        println("Receive reply ${reply.body()}")
        Assert.assertEquals("received: [$id]", reply.body())

    }

    @Test
    fun shouldHandleAnEventStreamSynchronously() = runBlocking<Unit> {

        val adaptor = streamAdaptor<Message<Int>>(vertx)
        vertx.eventBus().localConsumer<Int>("someAddress").handler(adaptor)

        //send 10 message to consumer
        for (i in 0..10) vertx.eventBus().send("someAddress", i)

        val count = AtomicInteger()

        //Receive 10 message from the consumer
        for (i in 0..10) {
            val message = adaptor.receive()
            println("got message: ${message.body()}")
            count.getAndIncrement()
        }

        Assert.assertEquals(11, count.get())
    }

    @Test
    fun shouldAwaitAFutureCompletion() = runBlocking<Unit> {
        val httpServerFuture = Future.future<HttpServer>()
        vertx.createHttpServer().requestHandler({ _ -> }).listen(8000, httpServerFuture.completer())
        //we can get httpServer by await on future instance.
        val httpServer = httpServerFuture.await()

        Assert.assertTrue(httpServerFuture.isComplete)
        Assert.assertNotNull(httpServer)
    }

    @Test
    fun shouldAwaitMultipleFuturesCompletion() = runBlocking<Unit> {
        val future1 = Future.future<HttpServer>()
        vertx.createHttpServer().requestHandler({ _ -> }).listen(8000, future1.completer())

        val future2 = Future.future<HttpServer>()
        vertx.createHttpServer().requestHandler({ _ -> }).listen(8001, future2.completer())

        val result = CompositeFuture.all(future1, future2).await()
        Assert.assertTrue(result.succeeded())
    }

    /*
    @Test
    fun testReceiveEventTimeout(testContext: TestContext) = runVertxCoroutine {
        val async = testContext.async()
        try {
            asyncEvent<Long>(250) { h -> vertx.setTimer(500, h) }
        } catch (npe: NullPointerException) {
            Assert.assertThat<NullPointerException>(npe, Is.isA<NullPointerException>(NullPointerException::class.java))
        } catch (e: Exception) {
            Assert.assertTrue(false)
        } finally {
            async.complete()
        }
    }
*/

    /*
    @Test
    fun testReceiveEventNoTimeout(testContext: TestContext) = runVertxCoroutine {
        val async = testContext.async()
        val start = System.currentTimeMillis()
        val tid = asyncEvent<Long>(1000L) { h -> vertx.setTimer(500, h) }.await()
        val end = System.currentTimeMillis()
        Assert.assertTrue(end - start >= 500)
        if (tid is Long) Assert.assertTrue(tid >= 0)
        else Assert.fail("can not cast tid type")
        async.complete()
    }
*/

    /*
    @Test
    fun testHandlerAdaptor(testContext: TestContext) {
        val async = testContext.async()
        val eb = vertx.eventBus()
        // Create a couple of consumers on different addresses
        // The adaptor allows handler to be used as a Channel
        val adaptor1 = streamAdaptor<Message<String>>()
        eb.consumer<String>(ADDRESS1).handler(adaptor1)

        val adaptor2 = streamAdaptor<Message<String>>()
        eb.consumer<String>(ADDRESS2).handler(adaptor2)

        // Set up a periodic timer to send messages to these addresses
        val start = System.currentTimeMillis()
        vertx.setPeriodic(10) { _ ->
            eb.send(ADDRESS1, "wibble")
            eb.send(ADDRESS2, "flibble")
        }
        runVertxCoroutine {
            for (i in 0..9) {
                val received1 = adaptor1.receive()
                Assert.assertEquals("wibble", received1.body())
                val received2 = adaptor2.receive()
                Assert.assertEquals("flibble", received2.body())
            }

            val end = System.currentTimeMillis()
            Assert.assertTrue(end - start >= 100)

            // Try a receive with timeout
            var received1 = adaptor1.receive(1000)

            if (received1 is Message<*>) Assert.assertEquals("wibble", received1.body())
            else Assert.fail("received1 cast type failed.")

            // And timing out
            val adaptor3 = streamAdaptor<Message<String>>()
            eb.consumer<String>(ADDRESS3).handler(adaptor3)
            val received3 = adaptor3.receive(100)
            Assert.assertNull(received3)

            // Try underlying channel
            val channel = adaptor1.channel
            Assert.assertNotNull(channel)
            received1 = channel.receive()
            Assert.assertEquals("wibble", received1.body())

            async.complete()
        }
    }
    */
}