package com.enricog.features.routines.list.usecase

import com.enricog.data.timer.api.theme.TimerThemeDataSource
import com.enricog.data.timer.api.theme.entities.TimerTheme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTimerThemeUserCase @Inject constructor(
    private val timerThemeDataSource: TimerThemeDataSource
){

    operator fun invoke(): Flow<TimerTheme> {
        return timerThemeDataSource.observeSelected()
    }
}