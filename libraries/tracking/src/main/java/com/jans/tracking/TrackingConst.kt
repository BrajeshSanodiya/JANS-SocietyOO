package com.jans.tracking

// Event Name used for tracking as EVENT KEY
object EventName {

    const val FIRST_LAUNCH = "New app Launch"
    const val FEED_VISITED = "Feed Visited"
    const val PROFILE_TAB_VISITED = "Profile Tab Visited"
    const val ARTICLE_OPENED = "Article Opened"
    const val ARTICLE_VIEWED = "Article Viewed"
    const val INLINE_ARTICLE_VIDEO_PLAYED = "Inline Article Video Played"
    const val ARTICLE_PREVIEWS_SCROLLED = "Article Previews Scrolled"
    const val FEED_REFRESHED = "Feed Refreshed"
    const val NEWS_CATEGORY_SELECTED = "News Category Selected"
    const val CONTENT_SAVED = "Content Saved"
    const val CONTENT_UNSAVED = "Content Unsaved"
    const val CONTENT_SHARED = "Content Shared"

    // Video
    const val VIDEO_TAB_VISITED = "Video Tab Visited"
    const val VIDEO_OPEN = "Video Open"
    const val VIDEO_VIEWED = "Video Viewed"
    const val VIDEO_SWIPED = "Video Swiped"

    // Epaper
    const val EPAPER_TAB_VISITED = "e-Paper Tab Visited"
    const val MAGAZINE_TAB_VISITED = "Magazine Tab Visited"
    const val EPAPER_OPENED = "e-Paper Opened"
    const val EPAPER_VIEWED = "e-Paper Viewed"

    // Notification
    const val NOTIFICATION_RECEIVED = "Notification Received"
    const val NOTIFICATION_DISPLAYED = "Notification Displayed"
    const val NOTIFICATION_DISMISSED = "Notification Dismissed"
    const val NOTIFICATION_INTERACTED = "Notification Interacted"
    const val NOTIFICATION_DECLINED = "Notification Declined"
}


// Property name used for tracking as Property KEY (key : value)
object PropertyName {

    const val LOGIN_STATUS = "Login Staus"
    const val DBID = "DBID"
    const val PROFILE_COMPLETED = "Profile Completed"
    const val APP_NAME = "Tracking App Name"
    const val FEED_TYPE = "Feed Type"

    // Notifications
    const val NOTIFICATION_TYPE = "Notification Type"
    const val NOTIFICATION_STYLE = "Notification Style"
    const val NOTIFICATION_TITLE = "Notification Title"
    const val NOTIFICATION_ID = "Notification ID"
    const val URL = "URL"
    const val NOTIFICATION_AGE = "Notification Age"
    const val REASON = "Reason"
    const val NOTIFICATIONS_RECEIVED = "Notifications Received"
    const val NOTIFICATIONS_DISPLAYED = "Notifications Displayed"
    const val NOTIFICATIONS_DISMISSED = "Notifications Dismissed"
    const val NOTIFICATIONS_DECLINED = "Notifications Declined"
    const val NOTIFICATIONS_INTERACTED = "Notifications Interacted"
    const val NOTIFICATION_INTERACTION_TYPE = "Notification Interaction Type"
}

// Property value used for tracking as Property VALUE (key : value)
object PropertyValue {
    const val RICH = "Rich"
    const val TEXT = "Text"
}