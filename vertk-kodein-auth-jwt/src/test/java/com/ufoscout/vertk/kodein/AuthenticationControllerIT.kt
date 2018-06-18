package com.ufoscout.vertk.kodein

import com.ufoscout.coreutils.auth.AuthService
import com.ufoscout.coreutils.jwt.kotlin.JwtService
import com.ufoscout.vertk.BaseIT
import com.ufoscout.vertk.kodein.auth.AuthContants
import com.ufoscout.vertk.kodein.auth.User
import com.ufoscout.vertk.kodein.web.ErrorDetails
import com.ufoscout.vertk.kodein.web.AuthenticationController
import com.ufoscout.vertk.kodein.web.LoginDto
import com.ufoscout.vertk.kodein.web.LoginResponseDto
import io.netty.handler.codec.http.HttpResponseStatus
import kotlinx.coroutines.experimental.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.kodein.di.generic.instance
import java.util.*


class AuthenticationControllerIT : BaseIT() {

    val client = vertk().createHttpClient()
    val jwt: JwtService = kodein().instance()
    val authService: AuthService<Long, User> = kodein().instance()

    @Test
    fun shouldCallLogin() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")
        val response = client.restPost<LoginResponseDto>(port(), "localhost", AuthenticationController.BASE_AUTH_API + "/login", loginDto)

        assertEquals(200, response.statusCode)
        logger.info("token is ${response.body!!.token}")

    }

    @Test
    fun shouldGetUnauthorizedWithAnonymousUser() = runBlocking<Unit> {
        val response = client.restGet<ErrorDetails>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/authenticated")

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("NotAuthenticated", response.body!!.message)
    }

    @Test

    fun shouldGetUnauthorizedWithAnonymousUserOnProtectedUri() = runBlocking<Unit> {
        val response = client.restGet<ErrorDetails>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected")

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("NotAuthenticated", response.body!!.message)
    }

    @Test
    fun shouldSuccessfulAccessAuthenticatedApiWithToken() = runBlocking<Unit> {

        val sentUserContext = User(0, UUID.randomUUID().toString(), authService.encode("ADMIN", "OTHER"))

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet<User>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/authenticated",
                headers)

        assertEquals(HttpResponseStatus.OK.code(), response.statusCode)

        val receivedUserContext = response.body
        assertNotNull(receivedUserContext)
        assertEquals(sentUserContext.username, receivedUserContext!!.username)
    }

    @Test
    fun shouldAccessPublicUriWithAnonymousUser() = runBlocking<Unit> {

        val response = client.restGet<User>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/public")

        val userContext = response.body
        assertNotNull(userContext)
        assertTrue(authService.decode(userContext!!.roles).size === 0)
        assertTrue(userContext!!.username.isEmpty())
    }


    @Test
    fun shouldSuccessfulLoginWithValidCredentials() = runBlocking<Unit> {

        val loginDto = LoginDto("user", "user")

        val response = client.restPost<LoginResponseDto>(
                port(),
                "localhost",
                AuthenticationController.BASE_AUTH_API + "/login",
                loginDto)


        val responseDto = response.body
        assertNotNull(responseDto)
        assertNotNull(responseDto!!.token)
        val userContext = jwt.parse(responseDto.token, User::class)
        assertEquals("user", userContext.username)
        assertEquals(1, authService.decode(userContext.roles).size)
        assertEquals("USER", authService.decode(userContext.roles)[0].name)
    }

    @Test
    @Throws(Exception::class)
    fun shouldFailLoginWithWrongCredentials() = runBlocking {

        val loginDto = LoginDto("user", UUID.randomUUID().toString())

        val response = client.restPost<ErrorDetails>(
                port(),
                "localhost",
                AuthenticationController.BASE_AUTH_API + "/login",
                loginDto)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("BadCredentials", response.body!!.message)
    }


    @Test
    fun shouldNotAccessProtectedApiWithoutAdminRole() = runBlocking {

        val sentUserContext = User(0, UUID.randomUUID().toString(), 0)

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet<ErrorDetails>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected",
                headers)

        assertEquals(HttpResponseStatus.FORBIDDEN.code(), response.statusCode)
        assertEquals("AccessDenied", response.body!!.message)

    }

    @Test
    fun shouldSuccessfulAccessProtectedApiWithAdminRole() = runBlocking {

        val sentUserContext = User(0, UUID.randomUUID().toString(), authService.encode("ADMIN"))

        val token = jwt.generate(sentUserContext)

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet<User>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected",
                headers)

        val receivedUserContext = response.body
        assertNotNull(receivedUserContext)
        assertEquals(sentUserContext.username, receivedUserContext!!.username)
    }

    @Test
    fun shouldGetTokenExpiredExceptionIfTokenNotValid() = runBlocking {

        val sentUserContext = User(0, UUID.randomUUID().toString(), 0)

        val token = jwt.generate("", sentUserContext, Date(System.currentTimeMillis() - 1000), Date(System.currentTimeMillis() - 1000))

        val headers = Pair(AuthContants.JWT_TOKEN_HEADER, "${AuthContants.JWT_TOKEN_HEADER_SUFFIX}$token")

        val response = client.restGet<ErrorDetails>(port(), "localhost",
                AuthenticationController.BASE_AUTH_API + "/test/protected",
                headers)

        assertEquals(HttpResponseStatus.UNAUTHORIZED.code(), response.statusCode)
        assertEquals("TokenExpired", response.body!!.message)

    }

}
