package com.ufoscout.vertk.kodein.mail

import io.vertx.ext.mail.MailMessage
import io.vertx.ext.mail.MailResult

interface MailClient {

    suspend fun sendEmail(mailMessage: MailMessage): MailResult

}