package com.orbita.finad.services

import android.content.Context
import android.provider.Settings

object NotificationPermissionChecker {

  fun isNotificationPermissionGranted(context: Context): Boolean {
    val packageName = context.packageName
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")

    return flat?.contains(packageName) == true
  }
}
