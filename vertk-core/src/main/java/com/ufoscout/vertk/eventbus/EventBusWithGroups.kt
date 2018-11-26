package com.ufoscout.vertk.eventbus

import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.shareddata.AsyncMap
import org.slf4j.LoggerFactory
import java.util.*

/**
 * A EventBusWithGroups is an EventBus based publisher/subscriber structure that permits to publish notifications to
 * groups of consumer.
 * The difference between the EventBus.publish() method is that when publishing on a EventBusWithGroups, only one consumer per each group
 * is notified.
 */
class EventBusWithGroups<T>(val vertx: Vertx, val address: String, val member: String) {

    companion object {

        private val LOG = LoggerFactory.getLogger(EventBusWithGroups::class.java)

        internal fun createAddressMember(address: String, route: String, member: String): String {
            return String.format("%s/%s/%s", address, route, member)
        }

        internal fun createAddressRoute(address: String, route: String): String {
            return String.format("%s/%s", address, route)
        }
    }

    private var table: AsyncMap<String, String>? = null

    /**
     * Register a consumer for a named group.
     * If more than one consumer for the same group is present, only one consumer will receive the message.
     */
    fun consume(groupName: String, handler: (Message<T>) -> Unit) {
        put(groupName) { vertx.eventBus().consumer<T>(it, handler) }
    }

    /**
     * Register a consumer for a named group.
     * If more than one consumer for the same group is present, only one consumer will receive the message.
     */
    fun awaitConsume(groupName: String, handler: suspend (T) -> Unit) {
        put(groupName) { vertx.eventBus().awaitConsumer<T>(it, handler) }
    }

    private fun put(route: String, handler: (String) -> Unit) {
        load { map ->
            val addressMember = createAddressMember(address, route, member)
            val addressRoute = createAddressRoute(address, route)
            map.putIfAbsent(addressMember, addressRoute) { ar ->
                if (ar.succeeded()) {
                    LOG.debug("Added - address: {}, publish: {}", address, route)
                    handler.invoke(addressRoute)
                } else {
                    LOG.error("Failed to add - address: {}, publish: {}", address, route, ar.cause())
                }
            }
        }
    }

    /**
     * ToDo: Remove the consumer based on the groupName
     * The current implementation does not work. The consumer should be removed from vertx eventbus
     */
    fun remove(groupName: String) {
        load { map ->
            val addressMember = createAddressMember(address, groupName, member)
            val addressRoute = createAddressRoute(address, groupName)
            map.removeIfPresent(addressMember, addressRoute) { ar ->
                if (ar.succeeded()) {
                    LOG.debug("Removed - address: {}, publish: {}", address, groupName)
                } else {
                    LOG.error("Failed to remove - address: {}, publish: {}", address, groupName, ar.cause())
                }
            }
        }
    }

    /**
     * Publish a message on the bus, exactly one consumer per group will receive it.
     */
    fun publish(message: T) {
        load { map ->
            map.values { ar ->
                if (ar.succeeded()) {
                    LOG.debug("Routing - address: {}, message: {}", address, message)
                    val routes = HashSet<String>(ar.result())
                    if (!routes.isEmpty()) {
                        routes.forEach { route -> vertx.eventBus().send(route, message) }
                    } else {
                        LOG.warn("No routes - address: {}, message: {}", address, message)
                    }
                } else {
                    LOG.error("Failed to publish - address: {}, message: {}", address, message, ar.cause())
                }
            }
        }
    }

    private fun load(handler: (AsyncMap<String, String>) -> Unit) {
        if (table != null) {
            handler.invoke(table!!)
        } else {
            vertx.sharedData().getAsyncMap<String, String>(address) { ar ->
                if (ar.succeeded()) {
                    LOG.debug("Loaded - address: {}", address)
                    table = ar.result()
                    handler.invoke(table!!)
                } else {
                    LOG.error("Failed to load - address: {}", address, ar.cause())
                }
            }
        }
    }

}