package com.enricog.data.local.database.timer.theme.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.enricog.data.local.database.timer.theme.model.InternalTimerTheme
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TimerThemeDao {

    @Transaction
    @Query("SELECT * FROM TimerThemes ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<InternalTimerTheme>>

    @Query("SELECT * FROM TimerThemes WHERE isDefault=1 LIMIT 1")
    suspend fun getDefault(): InternalTimerTheme
}
