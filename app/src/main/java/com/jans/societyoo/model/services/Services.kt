package com.jans.societyoo.model.services


import com.google.gson.annotations.SerializedName

data class Services(
    @SerializedName("services")
    val services: List<Service>,
    @SerializedName("micro-services")
    val microServices: List<MicroService>,
    @SerializedName("providers")
    val providers: List<Provider>

)