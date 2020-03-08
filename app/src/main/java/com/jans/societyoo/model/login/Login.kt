package com.jans.societyoo.model.login

import com.google.gson.annotations.SerializedName

data class OtpRequest(
    @SerializedName("mobile_no")
    val mobile_no: String
)

data class OtpVerifyRequest(
    @SerializedName("mobile_no")
    val mobile_no: String,
    @SerializedName("otp")
    val otp: String
)

data class SendOTPData(
    @SerializedName("data_details")
    val data_details: Int
)