package com.enricog.routines.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.R
import com.enricog.routines.list.models.RoutinesViewState
import com.enricog.routines.list.ui_components.EmptyScene
import com.enricog.routines.list.ui_components.RoutinesScene
import com.enricog.ui_components.ambients.navViewModel
import com.enricog.ui_components.common.toolbar.TempoToolbar

@Composable
internal fun RoutinesScreen(viewModel: RoutinesViewModel = navViewModel()) {
    val viewState by viewModel.viewState.collectAsState(RoutinesViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TempoToolbar(title = stringResource(R.string.title_routines))
        with(viewState) {
            when (this) {
                RoutinesViewState.Idle -> {
                }
                RoutinesViewState.Empty ->
                    EmptyScene(onCreateSegmentClick = viewModel::onCreateRoutineClick)
                is RoutinesViewState.Data ->
                    RoutinesScene(
                        routines = routines,
                        onRoutineClick = viewModel::onRoutineClick,
                        onRoutineDelete = viewModel::onRoutineDelete,
                        onCreateRoutineClick = viewModel::onCreateRoutineClick
                    )
            }.exhaustive
        }
    }
}
