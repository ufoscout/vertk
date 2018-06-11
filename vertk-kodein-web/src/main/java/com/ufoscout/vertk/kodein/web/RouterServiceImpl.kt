package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.Vertk
import com.ufoscout.vertk.kodein.config.RouterConfig
import com.ufoscout.vertk.web.Router
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.RoutingContext
import java.util.*

class RouterServiceImpl(val routerConfig: RouterConfig, val vertk: Vertk, val webExceptionService: WebExceptionService) : RouterService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val mainRouter = Router.router(vertk);

    init {
        mainRouter.route().failureHandler { handleFailure(it) }
    }

    override fun router(): Router {
        val router = Router.router(vertk)
        mainRouter.router().mountSubRouter(routerConfig.subRouterMountPoint, router.router())
        return router
    }

    override suspend fun start() {
        val port = routerConfig.port;
        vertk.createHttpServer().requestHandler(Handler<HttpServerRequest> { mainRouter.accept(it) }).listen(port)
        logger.info("Router created and listening on port ${port}")
    }

    private fun handleFailure(context: RoutingContext) {
        logger.info("Handling failure")
        val exception = context.failure()
        val response = context.response()

        if (exception is WebException) {
            reply(response, exception)
        } else {
            val webEx = webExceptionService.get(exception)
            if (webEx != null) {
                reply(response, webEx)
            } else {
                var statusCode = context.statusCode()
                if (statusCode < 0) {
                    statusCode = 500
                }
                val uuid = UUID.randomUUID().toString()
                val message = "Error code: " + uuid
                logger.error(uuid + " : " + exception.message, exception)
                endWithJson(response.setStatusCode(statusCode), ErrorDetails(statusCode, message))
            }
        }

    }

    private fun reply(response: HttpServerResponse, exception: WebException) {
        val statusCode = exception.statusCode()
        endWithJson(response.setStatusCode(statusCode), ErrorDetails(statusCode, exception.message!!))
    }

    private fun endWithJson(response: HttpServerResponse, obj: Any) {
        response.putHeader("Content-Type", "application/json; charset=utf-8").end(Json.encode(obj))
    }
}
