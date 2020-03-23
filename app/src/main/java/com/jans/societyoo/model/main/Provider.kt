package com.jans.societyoo.model.main


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "society_service_provider")
data class Provider(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("img")
    val img: String,

    @SerializedName("is_new")
    val isNew: Int,

    @SerializedName("micro_service_id")
    val microServiceId: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("service_id")
    val serviceId: Int
)