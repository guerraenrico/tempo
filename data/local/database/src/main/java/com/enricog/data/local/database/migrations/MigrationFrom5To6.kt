package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationFrom5To6 : Migration(5, 6) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        try {
            database.execSQL("ALTER TABLE Routines ADD COLUMN rounds INTEGER NOT NULL DEFAULT(1)")
            database.execSQL("ALTER TABLE Segments ADD COLUMN rounds INTEGER NOT NULL DEFAULT(1)")
            database.setTransactionSuccessful()
        } finally {
            database.endTransaction()
        }
    }
}
