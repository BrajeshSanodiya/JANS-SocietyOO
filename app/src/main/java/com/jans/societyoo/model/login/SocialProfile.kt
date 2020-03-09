package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class SocialProfile(
    @SerializedName("behance")
    val behance: String,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("github")
    val github: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("linkedin")
    val linkedin: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("twitter")
    val twitter: String,
    @SerializedName("website")
    val website: String
)