package com.enricog.features.routines.list

import app.cash.turbine.test
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.sortedByRank
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.features.routines.list.usecase.DeleteRoutineUseCase
import com.enricog.features.routines.list.usecase.DuplicateRoutineUseCase
import com.enricog.features.routines.list.usecase.GetRoutinesUseCase
import com.enricog.features.routines.list.usecase.MoveRoutineUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import com.enricog.navigation.api.routes.RoutineRoute
import com.enricog.navigation.api.routes.RoutineRouteInput
import com.enricog.navigation.api.routes.RoutineSummaryRoute
import com.enricog.navigation.api.routes.RoutineSummaryRouteInput
import com.enricog.navigation.testing.FakeNavigator
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.Routine as RoutineEntity

class RoutinesViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule(StandardTestDispatcher())

    private val firstRoutineEntity = RoutineEntity.EMPTY.copy(
        id = 1.asID,
        name = "First Routine",
        rank = Rank.from(value = "aaaaaa")
    )
    private val secondRoutineEntity = RoutineEntity.EMPTY.copy(
        id = 2.asID,
        name = "Second Routine",
        rank = Rank.from(value = "bbbbbb")
    )
    private val thirdRoutineEntity = RoutineEntity.EMPTY.copy(
        id = 3.asID,
        name = "Third Routine",
        rank = Rank.from(value = "cccccc")
    )
    private val firstRoutine = RoutinesItem.RoutineItem(
        id = 1.asID,
        name = "First Routine",
        rank = "aaaaaa",
        segmentsSummary = null
    )
    private val secondRoutine = RoutinesItem.RoutineItem(
        id = 2.asID,
        name = "Second Routine",
        rank = "bbbbbb",
        segmentsSummary = null
    )
    private val thirdRoutine = RoutinesItem.RoutineItem(
        id = 3.asID,
        name = "Third Routine",
        rank = "cccccc",
        segmentsSummary = null
    )
    private val navigator = FakeNavigator()
    private val store = FakeStore(
        initialValue = listOf(firstRoutineEntity, secondRoutineEntity, thirdRoutineEntity)
    )
    private val routineDataSource = FakeRoutineDataSource(store = store)

    @Test
    fun `should should show data when load succeeds`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                secondRoutine,
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should show error when load fails`() = coroutineRule {
        store.enableErrorOnNextAccess()

        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(RoutinesViewState.Error::class.java)
        }
    }

    @Test
    fun `should navigate to routine detail when create routine button clicked`() = coroutineRule {
        val viewModel = buildViewModel()

        viewModel.onCreateRoutine()

        navigator.assertGoTo(
            route = RoutineRoute,
            input = RoutineRouteInput(routineId = ID.new())
        )
    }

    @Test
    fun `should navigate to routine summary when routine clicked`() = coroutineRule {
        val routineId = 1.asID
        val viewModel = buildViewModel()

        viewModel.onRoutine(routineId = routineId)

        navigator.assertGoTo(
            route = RoutineSummaryRoute,
            input = RoutineSummaryRouteInput(routineId = 1.asID)
        )
    }

    @Test
    fun `should remove routine from list and show message when routine is deleted`() =
        coroutineRule {
            val expected = RoutinesViewState.Data(
                routinesItems = immutableListOf(secondRoutine, thirdRoutine, RoutinesItem.Space),
                message = Message(
                    textResId = R.string.label_routines_delete_confirm,
                    actionTextResId = R.string.action_text_routines_delete_undo
                )
            )
            val expectedDatabaseRoutines =
                listOf(firstRoutineEntity, secondRoutineEntity, thirdRoutineEntity)
            val viewModel = buildViewModel()
            advanceUntilIdle()

            viewModel.onRoutineDelete(routineId = firstRoutine.id)
            advanceUntilIdle()

            viewModel.viewState.test {
                assertThat(awaitItem()).isEqualTo(expected)
            }
            assertThat(store.get()).isEqualTo(expectedDatabaseRoutines)
        }

    @Test
    fun `should restore routine when undo delete routine is clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                secondRoutine,
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutines =
            listOf(firstRoutineEntity, secondRoutineEntity, thirdRoutineEntity)
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(store.get()).isEqualTo(expectedDatabaseRoutines)
    }

    @Test
    fun `should delete routine from database when snackbar is dismissed`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, thirdRoutine, RoutinesItem.Space),
            message = null
        )
        val expectedDatabaseRoutines = listOf(secondRoutineEntity, thirdRoutineEntity)
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(store.get()).isEqualTo(expectedDatabaseRoutines)
    }

    @Test
    fun `should delete routine in database when composable stops`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, thirdRoutine, RoutinesItem.Space),
            message = null
        )
        val expectedDatabaseRoutines = listOf(secondRoutineEntity, thirdRoutineEntity)
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()

        viewModel.onStop()
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(store.get()).isEqualTo(expectedDatabaseRoutines)
    }

    @Test
    fun `should show message when delete routine fails`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                secondRoutine,
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()

        viewModel.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should retry delete when when snackbar action is clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, thirdRoutine, RoutinesItem.Space),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        viewModel.onRoutineDelete(routineId = firstRoutine.id)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.ActionPerformed)
        advanceUntilIdle()
        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should duplicate routine when routine is duplicated`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                firstRoutine.copy(
                    id = 4.asID,
                    rank = "annnnn"
                ),
                secondRoutine,
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineDuplicate(routineId = firstRoutine.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should delete pending routine when another routine is duplicated`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                firstRoutine.copy(
                    id = 4.asID,
                    rank = "bbbbbb"
                ),
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutines = listOf(
            firstRoutineEntity,
            firstRoutineEntity.copy(id = 4.asID, rank = Rank.from("bbbbbb")),
            thirdRoutineEntity
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onRoutineDelete(routineId = secondRoutine.id)
        advanceUntilIdle()

        viewModel.onRoutineDuplicate(routineId = firstRoutine.id)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(store.get().sortedByRank()).isEqualTo(expectedDatabaseRoutines)
    }

    @Test
    fun `should show message when duplicate routine clicked and duplication fails`() =
        coroutineRule {
            val expected = RoutinesViewState.Data(
                routinesItems = immutableListOf(
                    firstRoutine,
                    secondRoutine,
                    thirdRoutine,
                    RoutinesItem.Space
                ),
                message = Message(
                    textResId = R.string.label_routines_duplicate_error,
                    actionTextResId = null
                )
            )
            val viewModel = buildViewModel()
            advanceUntilIdle()
            store.enableErrorOnNextAccess()

            viewModel.onRoutineDuplicate(routineId = firstRoutine.id)
            advanceUntilIdle()

            viewModel.viewState.test {
                assertThat(awaitItem()).isEqualTo(expected)
            }
        }

    @Test
    fun `should hide message when snackbar is dismissed`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                firstRoutine,
                secondRoutine,
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        store.enableErrorOnNextAccess()
        viewModel.onRoutineMoved(
            draggedRoutineId = thirdRoutine.id,
            hoveredRoutineId = secondRoutine.id
        )
        advanceUntilIdle()

        viewModel.onSnackbarEvent(TempoSnackbarEvent.Dismissed)
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should reload when retry button clicked`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(secondRoutine, RoutinesItem.Space),
            message = null
        )
        val viewModel = buildViewModel()
        store.update { routines -> routines.filter { it.id == secondRoutine.id } }

        viewModel.onRetryLoad()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should move routine when routine is moved`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                secondRoutine,
                firstRoutine.copy(rank = "booooo"),
                thirdRoutine,
                RoutinesItem.Space
            ),
            message = null
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()

        viewModel.onRoutineMoved(
            draggedRoutineId = firstRoutine.id,
            hoveredRoutineId = secondRoutine.id
        )
        advanceUntilIdle()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
    }

    @Test
    fun `should delete pending routine when routine is moved`() = coroutineRule {
        val expected = RoutinesViewState.Data(
            routinesItems = immutableListOf(
                secondRoutine,
                firstRoutine.copy(rank = "nnnnnn"),
                RoutinesItem.Space
            ),
            message = null
        )
        val expectedDatabaseRoutines = listOf(
            secondRoutineEntity,
            firstRoutineEntity.copy(rank = Rank.from(value = "nnnnnn"))
        )
        val viewModel = buildViewModel()
        advanceUntilIdle()
        viewModel.onRoutineDelete(routineId = thirdRoutine.id)
        advanceUntilIdle()

        viewModel.onRoutineMoved(
            draggedRoutineId = firstRoutine.id,
            hoveredRoutineId = secondRoutine.id
        )
        advanceUntilIdle()
        runCurrent()

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(expected)
        }
        assertThat(store.get().sortedByRank()).isEqualTo(expectedDatabaseRoutines)
    }

    private fun buildViewModel(): RoutinesViewModel {
        return RoutinesViewModel(
            dispatchers = coroutineRule.getDispatchers(),
            converter = RoutinesStateConverter(),
            navigationActions = RoutinesNavigationActions(navigator),
            reducer = RoutinesReducer(),
            getRoutinesUseCase = GetRoutinesUseCase(
                routineDataSource = routineDataSource
            ),
            deleteRoutineUseCase = DeleteRoutineUseCase(
                routineDataSource = routineDataSource
            ),
            moveRoutineUseCase = MoveRoutineUseCase(
                routineDataSource = routineDataSource
            ),
            duplicateRoutineUseCase = DuplicateRoutineUseCase(
                routineDataSource = routineDataSource
            )
        )
    }
}
