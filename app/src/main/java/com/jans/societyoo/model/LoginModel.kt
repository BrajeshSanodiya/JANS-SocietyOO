package com.jans.loginsample.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */

data class LoginModel(
    val userId: String,
    val displayName: String,
    val mobileNumber: String,
    val emailID: String
)


data class MobileOtpModel(
    val mobileNumber: String
)

