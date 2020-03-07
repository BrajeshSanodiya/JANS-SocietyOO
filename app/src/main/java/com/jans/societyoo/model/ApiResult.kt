package com.jans.societyoo.model

import com.google.gson.annotations.SerializedName
import com.jans.societyoo.utils.MyResult
import org.json.JSONArray
import org.json.JSONObject

data class ApiDataObject (
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data_details")
    val data_details: JSONObject,

    @SerializedName("dis_msg")
    val dis_msg: Int
)

data class ApiDataArray (
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data_details")
    val data_details: List<Any>,

    @SerializedName("dis_msg")
    val dis_msg: Int
)