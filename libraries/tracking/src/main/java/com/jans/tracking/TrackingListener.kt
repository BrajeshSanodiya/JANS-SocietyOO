package com.jans.tracking

import android.app.Application

interface TrackingListener {

    /****
     * Need to call this method in application class
     ***********/
    fun initializeTracking(
        application: Application, comscorePublisherId: String,
        comscoreSecretId: String, appName: String
    )

    /****
     * triggered tracking with below methods
     ***********/
    fun trackUserIdentity(userId: String, trackRequestOption: TrackingOptions)

    fun trackEvent(
        eventName: String,
        propertyMap: Map<String, Any?>,
        trackRequestOption: TrackingOptions
    )

    fun trackEvent(
        eventName: String,
        propertyMap: Map<String, Any?>,
        superPropertyMap: Map<String, Any?>? = null,
        incrementalMap: Map<String, Int>? = null,
        peopleMap: Map<String, Any?>? = null,
        distinctId: String? = null,
        trackRequestOption: TrackingOptions
    )

    fun trackFirebaseEvent(eventName: String, propertyMap: Map<String, Any?>)

    fun trackComscoreEvent(propertyMap: Map<String, String?>)
}

// Use this class to triggered tracking Ex: Tracking.trackEvent(params)
object Tracking : TrackingListener by TrackingTrigger()