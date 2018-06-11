package com.ufoscout.vertk.kodein.auth

import com.ufoscout.coreutils.auth.Auth

class User (private val username: String, private val roles: Long): Auth<Long> {

    override fun getRoles(): Long {
        return roles
    }

    override fun getUsername(): String {
        return username
    }

}