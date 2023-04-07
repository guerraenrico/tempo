package com.enricog.data.local.database.timer.theme.model

import com.enricog.core.entities.asID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.OffsetDateTime

internal class InternalTimerThemeTest {

    @Test
    fun `map to internal`() {
        val now = OffsetDateTime.now()
        val timerTheme = TimerTheme(
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
        val expected = InternalTimerTheme(
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

        val actual = timerTheme.toInternal()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `map to entity`() {
        val now = OffsetDateTime.now()
        val internalTimerTheme = InternalTimerTheme(
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
        val expected = TimerTheme(
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

        val actual = internalTimerTheme.toEntity()

        assertThat(actual).isEqualTo(expected)
    }
}
