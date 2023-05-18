package com.enricog.libraries.notification.api

interface PermissionManager {
    fun checkPermission(permission: Permission): Boolean

    suspend fun askPermission(permission: Permission)
}