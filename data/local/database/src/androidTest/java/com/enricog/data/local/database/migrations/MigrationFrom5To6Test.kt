package com.enricog.data.local.database.migrations

import androidx.room.testing.MigrationTestHelper
import androidx.room.util.useCursor
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.enricog.data.local.database.TempoDatabase
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class MigrationFrom5To6Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldApplyRankWhenDatabaseContainsRoutines() {
        helper.createDatabase(TEST_DB, 5).apply {
            execSQL(
                """
                INSERT INTO Routines (name, preparationTimeInSeconds, createdAt, updatedAt, rank)
                VALUES ('Routine 1', 0, '2022-12-25T17:49:30+01:00', '2022-12-25T17:49:30+01:00', 'bbbbbb')
            """.trimIndent()
            )

            execSQL(
                """
                INSERT INTO Segments (routineId_fk, name, timeInSeconds, type, rank)
                VALUES (1, 'Segment 1', 10, 'TIMER', 'bbbbbb')
            """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 6, true, MigrationFrom5To6)

        db.query("SELECT * FROM Routines ORDER BY createdAt DESC").useCursor {
            it.moveToNext()
            assertThat(it.getString(it.getColumnIndex("name"))).isEqualTo("Routine 1")
            assertThat(it.getInt(it.getColumnIndex("rounds"))).isEqualTo(1)
        }

        db.query("SELECT * FROM Segments").useCursor {
            it.moveToNext()
            assertThat(it.getString(it.getColumnIndex("name"))).isEqualTo("Segment 1")
            assertThat(it.getInt(it.getColumnIndex("rounds"))).isEqualTo(1)
        }
    }

    private companion object {
        private const val TEST_DB = "migration-test"
    }
}
