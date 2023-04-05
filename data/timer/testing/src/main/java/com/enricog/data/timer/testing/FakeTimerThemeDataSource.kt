package com.enricog.data.timer.testing

import com.enricog.data.local.testing.FakeStore
import com.enricog.data.timer.api.theme.TimerThemeDataSource
import com.enricog.data.timer.api.theme.entities.TimerTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FakeTimerThemeDataSource(
    private val store: FakeStore<List<TimerTheme>>
) : TimerThemeDataSource {

    override fun observeAll(): Flow<List<TimerTheme>> {
        return store.asFlow()
    }

    override fun observeSelected(): Flow<TimerTheme> {
        return store.asFlow().map { l -> l.first() }
    }

    override suspend fun getSelected(): TimerTheme {
        return store.get().first()
    }
}