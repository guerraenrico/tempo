package com.enricog.data.local.database.timer.theme

import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.timer.theme.model.InternalTimerTheme
import com.enricog.data.timer.api.theme.TimerThemeDataSource
import com.enricog.data.timer.api.theme.entities.TimerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class TimerThemeDataSourceImpl @Inject constructor(
    private val database: TempoDatabase
) : TimerThemeDataSource {

    override fun observeAll(): Flow<List<TimerTheme>> {
        return database.timerThemeDao().observeAll()
            .map { list -> list.map(InternalTimerTheme::toEntity) }
    }

    override fun observeSelected(): Flow<TimerTheme> {
        return database.timerThemeDao().observeDefault()
            .map(InternalTimerTheme::toEntity)
    }

    override suspend fun getSelected(): TimerTheme {
        return database.timerThemeDao().getDefault().toEntity()
    }
}