package com.ufoscout.vertxk.kodein

import com.ufoscout.coreutils.auth.Role
import com.ufoscout.coreutils.auth.RolesProvider

class InMemoryRolesProvider: RolesProvider {

    companion object {
        val roles = listOf(
                Role(0, "ADMIN", arrayOf()),
                Role(1, "USER", arrayOf())
        )
    }

    override fun getAll(): List<Role> {
        return roles
    }

}