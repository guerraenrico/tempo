package com.enricog.data.timer.api.theme

import com.enricog.data.timer.api.theme.entities.TimerTheme
import kotlinx.coroutines.flow.Flow

interface TimerThemeDataSource {
    fun observeAll(): Flow<List<TimerTheme>>
}