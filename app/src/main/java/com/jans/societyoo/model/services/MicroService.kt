package com.jans.societyoo.model.services


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "society_micro_service")
data class MicroService(
    @PrimaryKey
    @SerializedName("id")
    val id: Int =0,

    @SerializedName("service_id")
    val serviceId: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("is_new")
    val isNew: Int = 0,

    @SerializedName("header_title")
    val headerTitle: String ="",

    @SerializedName("img")
    val img: String =""
)