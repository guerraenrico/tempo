package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationFrom6To7 : Migration(6, 7) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.execSQL("ALTER TABLE Routines ADD COLUMN frequencyGoal TEXT")
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}
