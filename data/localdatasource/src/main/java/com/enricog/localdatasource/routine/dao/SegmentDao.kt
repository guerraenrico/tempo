package com.enricog.localdatasource.routine.dao

import androidx.room.*
import com.enricog.localdatasource.routine.model.InternalSegment

@Dao
internal interface SegmentDao {

    @Transaction
    @Insert
    suspend fun insert(vararg segments: InternalSegment): List<Long>

    @Update
    suspend fun update(vararg segments: InternalSegment)

    @Delete
    suspend fun delete(vararg segment: InternalSegment)

}