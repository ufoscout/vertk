package com.ufoscout.vertxk.kodein.web

import com.ufoscout.vertxk.kodein.auth.BadCredentialsException
import com.ufoscout.vertxk.kodein.auth.User
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService() {

    private val users = ConcurrentHashMap<String, User>()

    init {
        users["user"] = User("user", 2L)
        users["admin"] = User("admin", 3L)
    }

    fun login(username: String, password: String): User {
        val user = users[username]

        if (user == null || !password.equals(username)) {
            throw BadCredentialsException("")
        }

        return user

    }

}
