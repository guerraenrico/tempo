package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal object MigrationFrom2To3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Routines RENAME COLUMN 'startTimeOffsetInSeconds' TO 'preparationTimeInSeconds';")
    }
}