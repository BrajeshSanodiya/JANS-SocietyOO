package com.jans.societyoo.model


import com.google.gson.annotations.SerializedName

data class UserPostData(
    /*@SerializedName("id")
    val id: Int?=0,*/
    @SerializedName("body")
    val body: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int
)

data class UserData(
    @SerializedName("id")
    val id: Int?=0,
    @SerializedName("body")
    val body: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("userId")
    val userId: Int
)