package com.ufoscout.vertk.kodein.web

import com.ufoscout.coreutils.auth.Auth
import com.ufoscout.vertk.kodein.auth.BadCredentialsException
import java.util.concurrent.ConcurrentHashMap

class InMemoryUserService() {

    private val users = ConcurrentHashMap<String, Auth>()

    init {
        users["user"] = Auth(0, "user", arrayOf("USER"))
        users["admin"] = Auth(1, "admin", arrayOf("USER", "ADMIN"))
    }

    fun login(username: String, password: String): Auth {
        val user = users[username]

        if (user == null || !password.equals(username)) {
            throw BadCredentialsException("")
        }

        return user

    }

}
