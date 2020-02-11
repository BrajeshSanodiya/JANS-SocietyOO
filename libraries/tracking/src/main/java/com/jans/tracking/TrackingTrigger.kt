package com.jans.tracking

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
//import com.comscore.Analytics
//import com.comscore.PublisherConfiguration
import com.google.firebase.analytics.FirebaseAnalytics


class TrackingTrigger : TrackingListener, ActivityLifecycleCallbacks {

    private lateinit var firebaseAnalytics: FirebaseAnalytics


    /*************
     * Activity lifecycle methods
     * ********************************/
    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
//        mActivitiesBackStack.add(activity::class)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        // comScore
//        Analytics.notifyEnterForeground()
    }

    override fun onActivityPaused(activity: Activity) {
        // comScore
//        Analytics.notifyExitForeground()
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    /******
     * Initialize tracking
     * ********************************/
    override fun initializeTracking(
        application: Application,
        comscorePublisherId: String,
        comscoreSecretId: String,
        appName: String
    ) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(application)

        initializeComscore(application, comscorePublisherId, comscoreSecretId, appName)

        application.registerActivityLifecycleCallbacks(this)
    }

    /*******
     * Tracking firebase, comscore
     * ****************************/
    override fun trackUserIdentity(userId: String, trackRequestOption: TrackingOptions) {
        if (trackRequestOption.firebase) {
            trackingFirebaseUserIdentity(userId)
        }
    }

    override fun trackEvent(
        eventName: String,
        propertyMap: Map<String, Any?>,
        trackRequestOption: TrackingOptions
    ) {
        if (trackRequestOption.comscore) {
            trackingComscoreEvent(propertyMap)
        }

        if (trackRequestOption.firebase) {
            trackingFirebaseEvents(eventName, propertyMap)
        }
    }

    override fun trackEvent(
        eventName: String,
        propertyMap: Map<String, Any?>,
        superPropertyMap: Map<String, Any?>?,
        incrementalMap: Map<String, Int>?,
        peopleMap: Map<String, Any?>?,
        distinctId: String?,
        trackRequestOption: TrackingOptions
    ) {

        if (trackRequestOption.comscore) {
            trackingComscoreEvent(propertyMap, superPropertyMap, peopleMap)
        }

        if (trackRequestOption.firebase) {
            trackingFirebaseEvents(eventName, propertyMap, superPropertyMap, peopleMap)
        }
    }

    override fun trackFirebaseEvent(eventName: String, propertyMap: Map<String, Any?>) {
        trackingFirebaseEvents(eventName, propertyMap)
    }

    override fun trackComscoreEvent(propertyMap: Map<String, String?>) {
        trackingComscoreEvent(propertyMap)
    }


    /******
     * Firebase tracking
     * ****************************************************************/

    private fun trackingFirebaseUserIdentity(userId: String) {
        if (::firebaseAnalytics.isInitialized) {
            firebaseAnalytics.setUserId(userId)
        }
    }

    private fun trackingFirebaseEvents(
        eventName: String,
        propertyMap: Map<String, Any?>
    ) {
        if (::firebaseAnalytics.isInitialized) {
            val bundle = TrackingUtils.mapToBundle(propertyMap)
            firebaseAnalytics.logEvent(eventName, bundle)
        }
    }

    private fun trackingFirebaseEvents(
        eventName: String,
        propertyMap: Map<String, Any?>,
        superPropertyMap: Map<String, Any?>?,
        peopleMap: Map<String, Any?>?
    ) {
        if (::firebaseAnalytics.isInitialized) {
            val bundle = TrackingUtils.mapToBundle(propertyMap)

            superPropertyMap?.let {
                bundle.putAll(TrackingUtils.mapToBundle(superPropertyMap))
            }

            peopleMap?.let {
                bundle.putAll(TrackingUtils.mapToBundle(peopleMap))
            }

            firebaseAnalytics.logEvent(eventName, bundle)
        }
    }

    /***********
     * Comscore tracking
     * *****************************************************************/

    private fun initializeComscore(
        context: Context,
        publisherId: String,
        secretId: String,
        appName: String
    ) {
//        val myPublisherConfig = PublisherConfiguration.Builder()
//            .publisherId(publisherId)
//            .publisherSecret(secretId)
//            .applicationName(appName)
//            .vce(false)
//            .build()
//        Analytics.getConfiguration().addClient(myPublisherConfig)
//        Analytics.start(context)
    }

    private fun trackingBackgroundMusicStart() {
//        Analytics.notifyUxActive()
    }

    private fun trackingBackgroundMusicStop() {
//        Analytics.notifyUxInactive()
    }

    private fun trackingComscoreEvent(
        propertyMap: Map<String, Any?>
    ) {
        //Analytics.notifyViewEvent(TrackingUtils.convertMapAnyToMapString(propertyMap))
    }

    private fun trackingComscoreEvent(
        propertyMap: Map<String, Any?>,
        superPropertyMap: Map<String, Any?>?,
        peopleMap: Map<String, Any?>?
    ) {
//        Analytics.notifyViewEvent(TrackingUtils.convertMapAnyToMapString(propertyMap))
//
//        superPropertyMap?.let {
//            Analytics.notifyViewEvent(TrackingUtils.convertMapAnyToMapString(superPropertyMap))
//        }
//
//        peopleMap?.let {
//            Analytics.notifyViewEvent(TrackingUtils.convertMapAnyToMapString(peopleMap))
//        }
    }

}