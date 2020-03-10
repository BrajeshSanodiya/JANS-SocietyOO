package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class FlatDetail(
    @SerializedName("flat_alias")
    val flatAlias: String,
    @SerializedName("flat_area")
    val flatArea: String,
    @SerializedName("flat_floor_nu")
    val flatFloorNu: Int,
    @SerializedName("flat_id")
    val flatId: Int,
    @SerializedName("flat_nu")
    val flatNu: String,
    @SerializedName("society_address")
    val societyAddress: String,
    @SerializedName("society_city")
    val societyCity: String,
    @SerializedName("society_id")
    val societyId: Int,
    @SerializedName("society_name")
    val societyName: String,
    @SerializedName("tower_alias")
    val towerAlias: String,
    @SerializedName("tower_id")
    val towerId: Int,
    @SerializedName("tower_name")
    val towerName: String,
    @SerializedName("um_mobile")
    val umMobile: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("user_master_id")
    val userMasterId: Int,
    @SerializedName("user_type")
    val userType: String
)