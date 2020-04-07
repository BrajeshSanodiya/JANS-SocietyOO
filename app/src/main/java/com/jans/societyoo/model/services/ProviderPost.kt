package com.jans.societyoo.model.services

import com.google.gson.annotations.SerializedName

data class ProviderPost (
    @SerializedName("defult_user_id")
    val defult_user_id: Int,

    @SerializedName("service_id")
    val service_id: Int,

    @SerializedName("micro_service_id")
    val micro_service_id: Int,

    @SerializedName("provider_detail")
    val providerDetail: ProviderDetail
)