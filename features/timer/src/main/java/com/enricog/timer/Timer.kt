package com.enricog.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.Clock
import com.enricog.ui_components.common.button.IconButton
import com.enricog.ui_components.common.button.IconButtonColor
import com.enricog.ui_components.common.button.IconButtonSize

@Composable
fun Timer() {
    val viewModel: TimerViewModel = viewModel() // TODO should use by?
    val viewState = viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement. Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewState.value.Compose()
        Spacer(modifier = Modifier.height(50.dp))
        IconButton(onClick = {}, icon = vectorResource(R.drawable.ic_timer_stop), size = IconButtonSize.Large, color = IconButtonColor.Confirm)
        Spacer(modifier = Modifier.height(20.dp))
        IconButton(onClick = {}, icon = vectorResource(R.drawable.ic_timer_stop), size = IconButtonSize.Normal)
    }
}

@Composable
internal fun TimerViewState.Compose() {
    when (this) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> Clock(
            backgroundColor = colors.primary,
            timeInSeconds = timeInSeconds
        )
    }
}