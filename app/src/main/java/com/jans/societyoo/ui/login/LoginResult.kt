package com.jans.societyoo.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoginUserView? = null,
    val error: Int? = null
)
