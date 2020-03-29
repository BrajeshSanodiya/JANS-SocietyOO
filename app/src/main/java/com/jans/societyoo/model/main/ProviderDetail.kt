package com.jans.societyoo.model.main


import com.google.gson.annotations.SerializedName

data class ProviderDetail(
    @SerializedName("about")
    val about: String,
    @SerializedName("contact_information")
    val contactInformation: ContactInformation,
    @SerializedName("images")
    val images: List<String>,
    @SerializedName("location")
    val location: String,
    @SerializedName("logo")
    val logo: String,
    @SerializedName("service_title")
    val serviceTitle: String,
    @SerializedName("Services_Offered")
    val servicesOffered: String
)