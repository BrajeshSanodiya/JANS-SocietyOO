package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("fname")
    val fname: String,
    @SerializedName("lname")
    val lname: String,
    @SerializedName("mname")
    val mname: String
)