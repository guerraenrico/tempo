package com.enricog.features.timer.usecase

import com.enricog.data.timer.api.settings.TimerSettingsDataSource
import com.enricog.data.timer.api.settings.entities.TimerSettings
import com.enricog.libraries.notification.api.Permission
import com.enricog.libraries.notification.api.PermissionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetTimerSettingsUseCase @Inject constructor(
    private val timerSettingsDataSource: TimerSettingsDataSource,
    private val permissionManager: PermissionManager
) {

    operator fun invoke(): Flow<TimerSettings> {
        return timerSettingsDataSource.observe()
            .map { settings ->
                if (permissionManager.checkPermission(permission = Permission.NOTIFICATION)) {
                    settings
                } else {
                    settings.copy(runInBackgroundEnabled = false)
                }
            }
    }
}