package com.enricog.localdatasource.routine.dao

import androidx.room.*
import com.enricog.localdatasource.routine.model.InternalRoutine
import com.enricog.localdatasource.routine.model.InternalRoutineWithSegments

@Dao
internal interface RoutineDao {

    @Transaction
    @Query("SELECT * FROM Routines ORDER BY createdAt DESC")
    suspend fun getAll(): List<InternalRoutineWithSegments>

    @Transaction
    @Query("SELECT * FROM Routines WHERE routineId=:id LIMIT 1")
    suspend fun get(id: Long): InternalRoutineWithSegments

    @Insert
    suspend fun insert(vararg routines: InternalRoutine): List<Long>

    @Update
    suspend fun update(vararg routines: InternalRoutine)
}