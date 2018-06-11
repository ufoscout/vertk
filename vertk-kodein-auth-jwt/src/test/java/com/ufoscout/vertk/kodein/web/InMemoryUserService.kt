package com.ufoscout.vertk.kodein.web

import com.ufoscout.vertk.kodein.auth.BadCredentialsException
import com.ufoscout.vertk.kodein.auth.User
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
