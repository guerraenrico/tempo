package com.enricog.libraries.permission.api

interface PermissionManager {
    fun checkPermission(permission: Permission): Boolean
    suspend fun askPermission(permission: Permission)
}