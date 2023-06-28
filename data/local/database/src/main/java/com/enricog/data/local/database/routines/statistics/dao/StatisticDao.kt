package com.enricog.data.local.database.routines.statistics.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.enricog.data.local.database.routines.statistics.model.InternalStatistic

@Dao
internal interface StatisticDao {

    @Transaction
    @Query("SELECT * FROM RoutineStatistics ORDER BY createdAt DESC")
    suspend fun getAll(): List<InternalStatistic>

    @Transaction
    @Query("SELECT * FROM RoutineStatistics WHERE routineId_fk=:routineId ORDER BY createdAt DESC")
    suspend fun getAllByRoutine(routineId: Long): List<InternalStatistic>

    @Insert
    suspend fun insert(vararg statistic: InternalStatistic): List<Long>
}