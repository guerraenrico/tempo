package com.enricog.data.local.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.enricog.entities.Rank

internal object MigrationFrom1To2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE Routines ADD COLUMN rank TEXT NOT NULL DEFAULT('')")

        val numberRoutine = database.compileStatement("SELECT COUNT(*) FROM Routines")
            .simpleQueryForLong()

        if(numberRoutine == 0L) return

        var rankTop: Rank? = null
        for (num in 0L until numberRoutine) {

            val rank = if (rankTop == null) Rank.calculateFirst() else
                Rank.calculateBottom(rankTop)
            rankTop = rank

            database.execSQL(
                """
                    UPDATE Routines SET rank='$rank'
                    WHERE routineId = (SELECT routineId FROM Routines ORDER BY createdAt DESC LIMIT 1 OFFSET $num)
                """.trimIndent()
            )
        }
    }
}