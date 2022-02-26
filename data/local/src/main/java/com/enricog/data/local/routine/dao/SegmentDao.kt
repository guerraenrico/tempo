package com.enricog.data.local.routine.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.enricog.data.local.routine.model.InternalSegment

@Dao
internal interface SegmentDao {

    @Query("SELECT * FROM Segments ORDER BY rank DESC")
    suspend fun getAll(): List<InternalSegment>

    @Insert
    suspend fun insert(vararg segment: InternalSegment): List<Long>

    @Update
    suspend fun update(vararg segment: InternalSegment)

    @Delete
    suspend fun delete(vararg segment: InternalSegment)
}
