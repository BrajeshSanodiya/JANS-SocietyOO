package com.jans.societyoo.model.login


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "user_data_table")
data class UserDetail(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    @SerializedName("profile_id")
    val profileId: Int=0,

    @ColumnInfo(name = "about")
    @SerializedName("about")
    val about: String?=null,

    @ColumnInfo(name = "aniv_date")
    @SerializedName("aniv_date")
    val anivDate: String?=null,

    @ColumnInfo(name = "blood_group")
    @SerializedName("blood_group")
    val bloodGroup: String?=null,

    @ColumnInfo(name = "defult_flat_id")
    @SerializedName("defult_flat_id")
    val defaultFlatId: Int?=null,

    @ColumnInfo(name = "defult_user_id")
    @SerializedName("defult_user_id")
    var defaultUserId: Int?=null,

    @ColumnInfo(name = "dob")
    @SerializedName("dob")
    var dob: String?=null,

    @ColumnInfo(name = "email")
    @SerializedName("email")
    var email: String?=null,

    @ColumnInfo(name = "ext_number")
    @SerializedName("ext_number")
    val extNumber: String?=null,

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    var gender: String?=null,

    @ColumnInfo(name = "hobbies")
    @SerializedName("hobbies")
    val hobbies: String?=null,

    @ColumnInfo(name = "interests_in")
    @SerializedName("interests_in")
    val interestsIn: String?=null,

    @ColumnInfo(name = "mobile")
    @SerializedName("mobile")
    var mobile: String?=null,

    @ColumnInfo(name = "mobile2")
    @SerializedName("mobile2")
    var mobile2: String?=null,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    var name: String?=null,

    @ColumnInfo(name = "occupation")
    @SerializedName("occupation")
    val occupation: String?=null,

    @ColumnInfo(name = "occupation_at")
    @SerializedName("occupation_at")
    val occupationAt: String?=null,

    @ColumnInfo(name = "occupation_group")
    @SerializedName("occupation_group")
    val occupationGroup: String?=null,

    @ColumnInfo(name = "skils")
    @SerializedName("skils")
    val skils: String?=null,

    @Embedded
    @SerializedName("social_profile")
    val socialProfile: SocialProfile?=null,

    @ColumnInfo(name = "sun_sign")
    @SerializedName("sun_sign")
    val sunSign: String?=null,

    @ColumnInfo(name = "user_pic")
    @SerializedName("user_pic")
    val userPic: String?=null,

    @ColumnInfo(name = "whatsapp_number")
    @SerializedName("whatsapp_number")
    val whatsappNumber: String?=null
)