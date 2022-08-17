package com.enricog.features.routines.list

import androidx.lifecycle.viewModelScope
import com.enricog.base.viewmodel.BaseViewModel
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.logger.api.TempoLogger
import com.enricog.data.routines.api.entities.Routine
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.usecase.RoutinesUseCase
import com.enricog.features.routines.navigation.RoutinesNavigationActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
internal class RoutinesViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    converter: RoutinesStateConverter,
    private val navigationActions: RoutinesNavigationActions,
    private val reducer: RoutinesReducer,
    private val routinesUseCase: RoutinesUseCase
) : BaseViewModel<RoutinesState, RoutinesViewState>(
    initialState = RoutinesState.Idle,
    converter = converter,
    dispatchers = dispatchers
) {

    init {
        load()
    }

    private fun load() {
        routinesUseCase.getAll()
            .onEach { routines -> updateState { reducer.setup(routines) } }
            .catch { throwable ->
                TempoLogger.e(throwable = throwable, message = "Error loading routines")
                updateState { reducer.error(throwable = throwable) }
            }
            .launchIn(viewModelScope)
    }

    fun onCreateRoutineClick() {
        launch {
            navigationActions.goToRoutine(routineId = ID.new())
        }
    }

    fun onRoutineClick(routine: Routine) {
        launch {
            navigationActions.goToRoutineSummary(routineId = routine.id)
        }
    }

    fun onRoutineDelete(routine: Routine) {
        launchWhen<RoutinesState.Data> {
            routinesUseCase.delete(routine)
        }
    }

    fun onRetryLoadClick() {
        load()
    }
}
