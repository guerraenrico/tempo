package com.enricog.localdatasource.routine.dao

import androidx.room.*
import com.enricog.localdatasource.routine.model.InternalSegment

@Dao
internal interface SegmentDao {

    @Query("SELECT * FROM Segments")
    suspend fun getAll(): List<InternalSegment>

    @Insert
    suspend fun insert(vararg segment: InternalSegment): List<Long>

    @Update
    suspend fun update(vararg segment: InternalSegment)

    @Delete
    suspend fun delete(vararg segment: InternalSegment)

}