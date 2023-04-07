package com.enricog.features.routines.detail.segment.usecase

import com.enricog.data.timer.api.theme.TimerThemeDataSource
import com.enricog.data.timer.api.theme.entities.TimerTheme
import javax.inject.Inject

internal class GetTimerThemeUseCase @Inject constructor(
    private val timerThemeDataSource: TimerThemeDataSource
) {

    suspend operator fun invoke(): TimerTheme {
        return timerThemeDataSource.getSelected()
    }
}