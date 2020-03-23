package com.jans.societyoo.model.main


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "society_service")
data class Service(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("header_title")
    val headerTitle: String,

    @SerializedName("img")
    val img: String,

    @SerializedName("is_new")
    val isNew: Int,

    @SerializedName("name")
    val name: String
)