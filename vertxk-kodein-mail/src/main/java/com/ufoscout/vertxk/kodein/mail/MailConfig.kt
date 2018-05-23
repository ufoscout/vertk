package com.ufoscout.vertxk.kodein.mail

data class MailConfig (
        val clientType: String,
        val hostname: String,
        val port: Int,
        val username: String,
        val password: String
)