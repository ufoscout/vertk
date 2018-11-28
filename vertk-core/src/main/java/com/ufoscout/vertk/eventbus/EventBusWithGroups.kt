package com.ufoscout.vertk.eventbus

import com.ufoscout.vertk.shared.awaitGetAsyncMap
import com.ufoscout.vertk.shared.awaitPut
import com.ufoscout.vertk.shared.awaitRemoveIfPresent
import com.ufoscout.vertk.shared.awaitValues
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.shareddata.AsyncMap
import org.slf4j.LoggerFactory
import java.util.*


data class Addr<T>(val address: String)

/**
 * A EventBusWithGroups is an EventBus based publisher/subscriber structure that permits to publish notifications to
 * groups of consumer.
 * The difference between the EventBus.publish() method is that when publishing on a EventBusWithGroups, only one consumer per each group
 * is notified.
 */
class EventBusWithGroups<T>(val vertx: Vertx, val address: Addr<T>) {

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
    suspend fun consumer(groupName: String, handler: (Message<T>) -> Unit): GroupMessageConsumer<T> {
        val member = UUID.randomUUID().toString()
        return GroupMessageConsumer<T>(
                member = member,
                address = address,
                groupName = groupName,
                map = load(),
                vertxConsumer = vertx.eventBus().consumer<T>(put(groupName, member), handler)
        )
    }

    /**
     * Register a consumer for a named group.
     * If more than one consumer for the same group is present, only one consumer will receive the message.
     */
    suspend fun awaitConsumer(groupName: String, handler: suspend (T) -> Unit): GroupMessageConsumer<T> {
        val member = UUID.randomUUID().toString()
        return GroupMessageConsumer<T>(
                member = member,
                address = address,
                groupName = groupName,
                map = load(),
                vertxConsumer = vertx.eventBus().awaitConsumer<T>(put(groupName, member), handler)
        )
    }

    private suspend fun put(route: String, member: String): String {
        val map = load()
        val addressMember = createAddressMember(address.address, route, member)
        val addressRoute = createAddressRoute(address.address, route)

        map.awaitPut(addressMember, addressRoute)
        LOG.debug("Added - address: {}, publish: {}", address, route)

        return addressRoute
    }

    /**
     * Publish a message on the bus, exactly one consumer per group will receive it.
     */
    suspend fun publish(message: T) {
        val map = load()
        LOG.trace("Routing - address: {}, message: {}", address, message)
        val routes = HashSet<String>(map.awaitValues())
        if (!routes.isEmpty()) {
            routes.forEach { route -> vertx.eventBus().send(route, message) }
        } else {
            LOG.warn("No routes - address: {}, message: {}", address, message)
        }
    }

    private suspend fun load(): AsyncMap<String, String> {
        if (table == null) {
            table = vertx.sharedData().awaitGetAsyncMap<String, String>(address.address)
            LOG.debug("Loaded - address: {}", address)
        }
        return table!!
    }

}

class GroupMessageConsumer<T>(
        private val map: AsyncMap<String, String>,
        private val vertxConsumer: io.vertx.core.eventbus.MessageConsumer<T>,
        val groupName: String,
        val address: Addr<T>,
        val member: String
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(GroupMessageConsumer::class.java)
    }

    suspend fun unregister() {
        val addressMember = EventBusWithGroups.createAddressMember(address.address, groupName, member)
        val addressRoute = EventBusWithGroups.createAddressRoute(address.address, groupName)

        if (map.awaitRemoveIfPresent(addressMember, addressRoute)) {
            LOG.debug("Removed - address: {}, publish: {}", address, groupName)
            vertxConsumer.awaitUnregister()
        } else {
            LOG.error("Failed to remove - address: {}, publish: {}", address, groupName)
        }
    }

}