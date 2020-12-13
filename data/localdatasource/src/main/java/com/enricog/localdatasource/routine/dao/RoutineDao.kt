package com.enricog.localdatasource.routine.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.enricog.localdatasource.routine.model.InternalRoutineWithSegments

@Dao
internal interface RoutineDao {

    @Transaction
    @Query("SELECT * FROM Routines")
    suspend fun getAll(): List<InternalRoutineWithSegments>

}