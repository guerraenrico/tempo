package com.enricog.localdatasource.routine.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
