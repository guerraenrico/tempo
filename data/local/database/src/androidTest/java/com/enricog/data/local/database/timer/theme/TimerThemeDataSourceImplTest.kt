package com.enricog.data.local.database.timer.theme

import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.TestTempoDatabaseFactory
import com.enricog.data.local.database.timer.theme.model.InternalTimerTheme
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.OffsetDateTime

@RunWith(AndroidJUnit4::class)
internal class TimerThemeDataSourceImplTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var database: TempoDatabase
    private lateinit var dataSource: TimerThemeDataSourceImpl

    @Before
    fun setup() {
        database = TestTempoDatabaseFactory.create()
        dataSource = TimerThemeDataSourceImpl(database = database)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldObserveAllTimerThemes() = coroutineRule {
        val internalTimerTheme1 = baseInternalTimerTheme.copy(id = 1, isDefault = true)
        val internalTimerTheme2 = baseInternalTimerTheme.copy(id = 2, isDefault = false)
        database.timerThemeDao().insert(internalTimerTheme1)
        database.timerThemeDao().insert(internalTimerTheme2)
        val expected = listOf(
            baseTimerTheme.copy(id = 1.asID, isDefault = true),
            baseTimerTheme.copy(id = 2.asID, isDefault = false)
        )

        dataSource.observeAll().test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun shouldObserveSelectedTimerTheme() = coroutineRule {
        val internalTimerTheme1 = baseInternalTimerTheme.copy(id = 1, isDefault = true)
        val internalTimerTheme2 = baseInternalTimerTheme.copy(id = 2, isDefault = false)
        database.timerThemeDao().insert(internalTimerTheme1)
        database.timerThemeDao().insert(internalTimerTheme2)
        val expected = baseTimerTheme.copy(id = 1.asID, isDefault = true)

        dataSource.observeSelected().test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun shouldGetSelectedTimerTheme() = coroutineRule {
        val internalTimerTheme1 = baseInternalTimerTheme.copy(id = 1, isDefault = true)
        val internalTimerTheme2 = baseInternalTimerTheme.copy(id = 2, isDefault = false)
        database.timerThemeDao().insert(internalTimerTheme1)
        database.timerThemeDao().insert(internalTimerTheme2)
        val expected = baseTimerTheme.copy(id = 1.asID, isDefault = true)

        val actual = dataSource.getSelected()

        assertThat(actual).isEqualTo(expected)
    }


    private val now = OffsetDateTime.now()
    private val baseInternalTimerTheme = InternalTimerTheme(
        id = 1,
        name = "name",
        description = "description",
        preparationTimeResource = InternalTimerTheme.InternalResource(
            background = InternalTimerTheme.InternalAsset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = InternalTimerTheme.InternalAsset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        stopwatchResource = InternalTimerTheme.InternalResource(
            background = InternalTimerTheme.InternalAsset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = InternalTimerTheme.InternalAsset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        timerResource = InternalTimerTheme.InternalResource(
            background = InternalTimerTheme.InternalAsset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = InternalTimerTheme.InternalAsset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        restResource = InternalTimerTheme.InternalResource(
            background = InternalTimerTheme.InternalAsset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = InternalTimerTheme.InternalAsset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        isLocked = true,
        isDefault = true,
        createdAt = now,
        updatedAt = now
    )
    private val baseTimerTheme = TimerTheme(
        id = 1.asID,
        name = "name",
        description = "description",
        preparationTimeResource = TimerTheme.Resource(
            background = TimerTheme.Asset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = TimerTheme.Asset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        stopwatchResource = TimerTheme.Resource(
            background = TimerTheme.Asset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = TimerTheme.Asset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        timerResource = TimerTheme.Resource(
            background = TimerTheme.Asset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = TimerTheme.Asset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        restResource = TimerTheme.Resource(
            background = TimerTheme.Asset.Color(
                argb = "18374687574888284160".toULong()
            ),
            onBackground = TimerTheme.Asset.Color(
                argb = "18446744069414584320".toULong()
            )
        ),
        isLocked = true,
        isDefault = true,
        createdAt = now,
        updatedAt = now
    )
}