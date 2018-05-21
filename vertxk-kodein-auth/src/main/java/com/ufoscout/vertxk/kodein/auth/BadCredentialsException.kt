package com.ufoscout.vertxk.kodein.auth

/**
 * Unauthorized Exception thrown when an invalid user calls a protected end point
 *
 * @author Francesco Cina'
 */
class BadCredentialsException(message: String) : RuntimeException(message) {
    companion object {
        private val serialVersionUID = 1L
    }
}
