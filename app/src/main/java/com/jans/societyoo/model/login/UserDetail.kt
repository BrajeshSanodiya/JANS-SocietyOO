package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("about")
    val about: String?=null,
    @SerializedName("aniv_date")
    val anivDate: String?=null,
    @SerializedName("blood_group")
    val bloodGroup: String?=null,
    @SerializedName("defult_flat_id")
    val defultFlatId: Int?=null,
    @SerializedName("defult_user_id")
    val defultUserId: Int?=null,
    @SerializedName("dob")
    val dob: String?=null,
    @SerializedName("email")
    val email: String?=null,
    @SerializedName("ext_number")
    val extNumber: String?=null,
    @SerializedName("gender")
    val gender: String?=null,
    @SerializedName("hobbies")
    val hobbies: String?=null,
    @SerializedName("interests_in")
    val interestsIn: String?=null,
    @SerializedName("mobile")
    val mobile: String?=null,
    @SerializedName("mobile2")
    val mobile2: String?=null,
    @SerializedName("name")
    val name: String?=null,
    @SerializedName("occupation")
    val occupation: String?=null,
    @SerializedName("occupation_at")
    val occupationAt: String?=null,
    @SerializedName("occupation_group")
    val occupationGroup: String?=null,
    @SerializedName("profile_id")
    val profileId: Int?=null,
    @SerializedName("skils")
    val skils: String?=null,
    @SerializedName("social_profile")
    val socialProfile: SocialProfile?=null,
    @SerializedName("sun_sign")
    val sunSign: String?=null,
    @SerializedName("user_pic")
    val userPic: String?=null,
    @SerializedName("whatsapp_number")
    val whatsappNumber: String?=null
)