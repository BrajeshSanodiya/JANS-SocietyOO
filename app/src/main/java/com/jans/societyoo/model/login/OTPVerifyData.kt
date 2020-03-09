package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class OTPVerifyData(
    @SerializedName("flat_details")
    val flatsDetails: List<FlatDetail>,
    @SerializedName("user_details")
    val userDetails: UserDetail
)