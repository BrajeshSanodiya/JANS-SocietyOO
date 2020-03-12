package com.jans.societyoo.model.login


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "flat_data_table")
data class FlatDetail(
    //@PrimaryKey(autoGenerate = true)
    @PrimaryKey
    @ColumnInfo(name = "flat_id")
    @SerializedName("flat_id")
    val flatId: Int,

    @ColumnInfo(name = "flat_alias")
    @SerializedName("flat_alias")
    val flatAlias: String,

    @ColumnInfo(name = "flat_area")
    @SerializedName("flat_area")
    val flatArea: String,

    @ColumnInfo(name = "flat_floor_nu")
    @SerializedName("flat_floor_nu")
    val flatFloorNu: Int,

    @ColumnInfo(name = "flat_nu")
    @SerializedName("flat_nu")
    val flatNu: String,

    @ColumnInfo(name = "society_address")
    @SerializedName("society_address")
    val societyAddress: String,

    @ColumnInfo(name = "society_city")
    @SerializedName("society_city")
    val societyCity: String,

    @ColumnInfo(name = "society_id")
    @SerializedName("society_id")
    val societyId: Int,

    @ColumnInfo(name = "society_name")
    @SerializedName("society_name")
    val societyName: String,

    @ColumnInfo(name = "tower_alias")
    @SerializedName("tower_alias")
    val towerAlias: String,

    @ColumnInfo(name = "tower_id")
    @SerializedName("tower_id")
    val towerId: Int,

    @ColumnInfo(name = "tower_name")
    @SerializedName("tower_name")
    val towerName: String,

    @ColumnInfo(name = "um_mobile")
    @SerializedName("um_mobile")
    val umMobile: String,

    @ColumnInfo(name = "user_id")
    @SerializedName("user_id")
    val userId: Int,

    @ColumnInfo(name = "user_master_id")
    @SerializedName("user_master_id")
    val userMasterId: Int,

    @ColumnInfo(name = "user_type")
    @SerializedName("user_type")
    val userType: String
)