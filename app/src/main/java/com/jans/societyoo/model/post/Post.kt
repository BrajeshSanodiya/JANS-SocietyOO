package com.jans.societyoo.model.post


import com.google.gson.annotations.SerializedName

data class Post(

    @SerializedName("id")
    val id: Int ?=null,

    @SerializedName("desc")
    val desc: String,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("publish_time")
    val publishTime: String ?=null,

    @SerializedName("user_id")
    val userId: Int ?=null,

    @SerializedName("user_img")
    val userImg: String ?=null,

    @SerializedName("user_name")
    val userName: String ?=null
)

data class CreatePost(
    @SerializedName("default_user_id")
    val default_user_id: Int,

    @SerializedName("default_flat_id")
    val default_flat_id: Int,

    @SerializedName("post_details")
    val post_details: Post
)