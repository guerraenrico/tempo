package com.enricog.features.routines.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.compose.testing.invoke
import com.enricog.core.entities.asID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.ui_components.RoutinesEmptySceneTestTag
import com.enricog.features.routines.list.ui_components.RoutinesErrorSceneTestTag
import com.enricog.features.routines.list.ui_components.RoutinesSceneTestTag
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutinesScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldNotRenderAnySceneWhenStateIsIdle() = composeRule {
        val viewState = RoutinesViewState.Idle

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRoutineDuplicate = {},
                    onRoutineMoved = { _, _ -> },
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderEmptySceneWhenStateIsEmpty() = composeRule {
        val viewState = RoutinesViewState.Empty

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRoutineDuplicate = {},
                    onRoutineMoved = { _, _ -> },
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutinesSceneWhenStateIsData() = composeRule {
        val timerTheme = TimerTheme.DEFAULT
        val viewState = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                RoutinesItem.RoutineItem(
                    id = 0.asID,
                    name = "Routine",
                    rank = "aaaaaa",
                    segmentsSummary = RoutinesItem.RoutineItem.SegmentsSummary(
                        estimatedTotalTime = "12".timeText,
                        segmentTypesCount = immutableMapOf(
                            TimeTypeStyle.from(timeType = TimeTypeEntity.TIMER, timerTheme = timerTheme) to 2,
                            TimeTypeStyle.from(timeType = TimeTypeEntity.REST, timerTheme = timerTheme) to 1,
                            TimeTypeStyle.from(timeType = TimeTypeEntity.STOPWATCH, timerTheme = timerTheme) to 1
                        )
                    )
                ),
                RoutinesItem.Space
            ),
            message = null
        )

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRoutineDuplicate = {},
                    onRoutineMoved = { _, _ -> },
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertIsDisplayed()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutinesErrorSceneWhenStateIsError() = composeRule {
        val exception = Exception("something went wrong")
        val viewState = RoutinesViewState.Error(throwable = exception)

        setContent {
            TempoTheme {
                viewState.Compose(
                    onCreateRoutine = {},
                    onRoutine = {},
                    onRoutineDelete = {},
                    onRoutineDuplicate = {},
                    onRoutineMoved = { _, _ -> },
                    onRetryLoad = {},
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutinesEmptySceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesSceneTestTag).assertDoesNotExist()
        onNodeWithTag(RoutinesErrorSceneTestTag).assertIsDisplayed()
    }
}
