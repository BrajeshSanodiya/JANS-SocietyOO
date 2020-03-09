package com.jans.societyoo.model.login


import com.google.gson.annotations.SerializedName

data class UserDetail(
    @SerializedName("about")
    val about: String,
    @SerializedName("aniv_date")
    val anivDate: String,
    @SerializedName("blood_group")
    val bloodGroup: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("ext_number")
    val extNumber: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("hobbies")
    val hobbies: String,
    @SerializedName("interests_in")
    val interestsIn: String,
    @SerializedName("2nd_phone_number")
    val ndPhoneNumber: String,
    @SerializedName("occupation")
    val occupation: String,
    @SerializedName("occupation_at")
    val occupationAt: String,
    @SerializedName("occupation_group")
    val occupationGroup: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("skils")
    val skils: String,
    @SerializedName("social_profile")
    val socialProfile: SocialProfile,
    @SerializedName("sun_sign")
    val sunSign: String,
    @SerializedName("user_defult_flat_id")
    val userDefultFlatId: Int,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_pic")
    val userPic: String,
    @SerializedName("user_profile_id")
    val userProfileId: Int,
    @SerializedName("whatsapp_number")
    val whatsappNumber: String
)