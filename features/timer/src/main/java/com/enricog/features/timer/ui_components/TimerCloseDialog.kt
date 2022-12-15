package com.enricog.features.timer.ui_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.timer.R
import com.enricog.ui.components.dialog.TempoDialogAction
import com.enricog.ui.components.dialog.TempoDialogAlert

internal const val TimerCloseDialogTestTag = "TimerCloseDialogTestTag"

@Composable
fun TimerCloseDialog(
    onClose: () -> Unit,
    onDismiss: () -> Unit
) {
    TempoDialogAlert(
        modifier = Modifier.testTag(TimerCloseDialogTestTag),
        title = stringResource(R.string.dialog_exit_time_title),
        description = stringResource(R.string.dialog_exit_time_description),
        positiveAction = TempoDialogAction(
            text = stringResource(R.string.dialog_exit_time_action_positive),
            onClick = onClose,
            contentDescription = stringResource(R.string.content_description_dialog_quite_routine_button_positive)
        ),
        negativeAction = TempoDialogAction(
            text = stringResource(R.string.dialog_exit_time_action_negative),
            onClick = onDismiss,
            contentDescription = stringResource(R.string.content_description_dialog_quite_routine_button_negative)
        ),
        onDismiss = onDismiss,
        isCancellable = true
    )
}