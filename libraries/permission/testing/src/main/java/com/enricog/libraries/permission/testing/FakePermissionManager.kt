package com.enricog.libraries.permission.testing

import com.enricog.libraries.permission.api.Permission
import com.enricog.libraries.permission.api.PermissionManager
import com.google.common.truth.Truth.assertThat

class FakePermissionManager : PermissionManager {

    private var permissions = emptyMap<Permission, Boolean>()
    private val askedPermissions = mutableListOf<Permission>()

    fun setPermission(vararg value: Pair<Permission, Boolean>) {
        permissions = value.toMap()
    }

    fun isPermissionAsked(permission: Permission) {
        assertThat(askedPermissions).contains(permission)
    }

    override fun checkPermission(permission: Permission): Boolean {
        return permissions[permission] ?: throw IllegalStateException("Permission $permission not set")
    }

    override suspend fun askPermission(permission: Permission) {
        askedPermissions.add(permission)
    }
}