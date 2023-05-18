package com.enricog.libraries.notification.api

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class PermissionManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val navigationActions: PermissionNavigationActions
) : PermissionManager {

    override fun checkPermission(permission: Permission): Boolean {
        return when (permission) {
            Permission.NOTIFICATION -> NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    override suspend fun askPermission(permission: Permission) {
        when (permission) {
            Permission.NOTIFICATION -> navigationActions.openNotificationRationale()
        }
    }
}