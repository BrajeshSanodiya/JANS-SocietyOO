package com.jans.societyoo.model

import com.google.gson.annotations.SerializedName
import com.jans.societyoo.utils.MyResult
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

data class ApiDataObject<out T : Any> (
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data_details")
    val data_details: T,

    @SerializedName("dis_msg")
    val dis_msg: Int
)

data class ApiDataWithOutObject (
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("dis_msg")
    val dis_msg: Int
)

/*data class ApiDataArray (
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data_details")
    val data_details: List<Any>,

    @SerializedName("dis_msg")
    val dis_msg: Int
)*/


data class ApiDataFile(
    @SerializedName("success_stat")
    val success_stat: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data_details")
    val data_details: imageObject,

    @SerializedName("dis_msg")
    val dis_msg: Int
)

data class imageObject(
    @SerializedName("link")
    val link: String
)

data class ApiDataFileBhaskar(
    @SerializedName("status")
    val status: String,

    @SerializedName("code")
    val code: String,

    @SerializedName("filename")
    val filename: String,

    @SerializedName("msg")
    val msg: String
)


data class ImageUploadRequest(
    @SerializedName("image")
    val image: RequestBody,

    @SerializedName("type")
    val type: Int,

    @SerializedName("name")
    val name: String
)
