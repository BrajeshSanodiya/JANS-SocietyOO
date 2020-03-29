package com.jans.societyoo.model.main


import com.google.gson.annotations.SerializedName

data class ContactInformation(
    @SerializedName("address")
    val address: String,
    @SerializedName("contact_person")
    val contactPerson: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("whats_app")
    val whatsApp: String,
    @SerializedName("working_hours")
    val workingHours: String
)