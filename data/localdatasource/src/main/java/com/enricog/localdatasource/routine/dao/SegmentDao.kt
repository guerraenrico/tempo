package com.enricog.localdatasource.routine.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Transaction
import androidx.room.Update
import com.enricog.localdatasource.routine.model.InternalSegment

@Dao
internal interface SegmentDao {

    @Transaction
    @Insert
    suspend fun insert(vararg segments: InternalSegment): List<Long>

    @Update
    suspend fun update(vararg segments: InternalSegment)

}