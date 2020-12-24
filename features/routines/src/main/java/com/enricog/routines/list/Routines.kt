package com.enricog.routines.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.routines.list.models.RoutinesViewState

@Composable
internal fun Routines(viewModel: RoutinesViewModel) {
    val viewState = viewModel.viewState.collectAsState(RoutinesViewState.Idle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        viewState.value.Compose()
    }
}

@Composable
private fun RoutinesViewState.Compose() {
    when(this) {
        RoutinesViewState.Idle -> {}

    }.exhaustive
}
