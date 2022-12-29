package com.enricog.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.enricog.data.local.database.converter.OffsetDateTimeConverter
import com.enricog.data.local.database.converter.TimeTypeConverter
import com.enricog.data.local.database.migrations.MigrationFrom1To2
import com.enricog.data.local.database.routines.dao.RoutineDao
import com.enricog.data.local.database.routines.dao.SegmentDao
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.local.database.routines.model.InternalSegment

@Database(
    entities = [
        InternalRoutine::class,
        InternalSegment::class,
    ],
    version = 2
)
@TypeConverters(
    TimeTypeConverter::class,
    OffsetDateTimeConverter::class,
)
internal abstract class TempoDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao

    abstract fun segmentDao(): SegmentDao

    companion object {
        private var INSTANCE: TempoDatabase? = null

        fun getInstance(context: Context): TempoDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): TempoDatabase {
            return Room
                .databaseBuilder(context.applicationContext, TempoDatabase::class.java, "Tempo.db")
                .addMigrations(MigrationFrom1To2)
                .build()
        }
    }
}
