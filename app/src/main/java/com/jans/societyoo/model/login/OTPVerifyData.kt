package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class OTPVerifyData(
    @SerializedName("flat_details")
    val flats: List<Flat>,
    @SerializedName("user_profile")
    val userProfile: UserProfile
)