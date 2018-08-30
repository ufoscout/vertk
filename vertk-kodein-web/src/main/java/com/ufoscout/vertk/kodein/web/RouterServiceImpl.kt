package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.awaitListen
import com.ufoscout.vertk.web.endWithJson
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.util.*

class RouterServiceImpl(val routerConfig: RouterConfig,
                        val httpServerOptions: HttpServerOptions,
                        val vertx: Vertx,
                        val webExceptionService: WebExceptionService) : RouterService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val mainRouter = Router.router(vertx);

    init {
        mainRouter.route().failureHandler { handleFailure(it) }
    }

    override fun router(): Router {
        val router = Router.router(vertx)
        mainRouter.mountSubRouter(routerConfig.subRouterMountPoint, router)
        return router
    }

    override suspend fun start() {
        val port = vertx.createHttpServer(httpServerOptions)
                .requestHandler(Handler<HttpServerRequest> { mainRouter.accept(it) })
                .awaitListen(routerConfig.port).actualPort()
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
                response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, message, WebException.DEFAULT_DETAILS))
            }
        }

    }

    private fun reply(response: HttpServerResponse, exception: WebException) {
        logger.error(exception.message, exception)
        val statusCode = exception.statusCode()
        response.setStatusCode(statusCode).endWithJson(ErrorDetails(statusCode, message(exception), exception.details()))
    }

    private fun message(exception: WebException): String {
        if (exception.message!=null) {
            return exception.message
        }
        return ""
    }

}
