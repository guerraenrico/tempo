package com.enricog.data.local.database.migrations

import androidx.core.database.getStringOrNull
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
class MigrationFrom6To7Test {

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        TempoDatabase::class.java
    )

    @Test
    fun shouldAddColumnFrequencyGoalToRoutines() {
        helper.createDatabase(TEST_DB, 6).apply {
            execSQL(
                """
                    INSERT INTO Routines (name, preparationTimeInSeconds, createdAt, updatedAt, rank, rounds)
                    VALUES ('Routine 1', 0, '2022-12-25T17:49:30+01:00', '2022-12-25T17:49:30+01:00', 'bbbbbb', 1)
                """.trimIndent()
            )
            close()
        }

        val db = helper.runMigrationsAndValidate(TEST_DB, 7, true, MigrationFrom6To7)

        db.query("SELECT * FROM Routines ORDER BY createdAt DESC").useCursor {
            it.moveToNext()
            assertThat(it.getString(it.getColumnIndex("name"))).isEqualTo("Routine 1")
            assertThat(it.getStringOrNull(it.getColumnIndex("frequencyGoal"))).isNull()
        }
    }

    private companion object {
        private const val TEST_DB = "migration-test"
    }
}