package com.enricog.data.local.routine.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.enricog.data.local.routine.model.InternalRoutine
import com.enricog.data.local.routine.model.InternalRoutineWithSegments
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RoutineDao {

    @Transaction
    @Query("SELECT * FROM Routines ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<InternalRoutineWithSegments>>

    @Transaction
    @Query("SELECT * FROM Routines WHERE routineId=:id LIMIT 1")
    fun observe(id: Long): Flow<InternalRoutineWithSegments>

    @Transaction
    @Query("SELECT * FROM Routines ORDER BY createdAt DESC")
    suspend fun getAll(): List<InternalRoutineWithSegments>

    @Transaction
    @Query("SELECT * FROM Routines WHERE routineId=:id LIMIT 1")
    suspend fun get(id: Long): InternalRoutineWithSegments

    @Insert
    suspend fun insert(vararg routine: InternalRoutine): List<Long>

    @Update
    suspend fun update(vararg routine: InternalRoutine)

    @Delete
    suspend fun delete(vararg routine: InternalRoutine)
}
