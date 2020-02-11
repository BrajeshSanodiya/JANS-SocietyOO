package com.jans.tracking

data class TrackingOptions(
    val firebase: Boolean = true,
    val comscore: Boolean = false
)


//data class TrackingData(
//    val eventName: String,
//    val propertyMap: Map<String, String?>,
//    val superPropertyMap: Map<String, String?>? = null,
//    val incrementalMap: Map<String, Int>? = null,
//    val peopleMap: Map<String, String?>? = null,
//    val distinctId: String? = null,
//    val trackRequestOption: TrackingOptions
//)
