package com.enricog.features.timer.usecase

import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.libraries.permission.api.Permission
import com.enricog.libraries.permission.api.PermissionManager
import javax.inject.Inject

internal class UpdateTimerSettingsUseCase @Inject constructor(
    private val timerSettingsDataSource: TimerSettingsDataSource,
    private val permissionManager: PermissionManager
) {

    suspend operator fun invoke(settings: TimerSettings) {
        if (settings.runInBackgroundEnabled && !permissionManager.checkPermission(permission = Permission.NOTIFICATION)) {
            permissionManager.askPermission(permission = Permission.NOTIFICATION)
        } else {
            timerSettingsDataSource.update(settings = settings)
        }
    }
}