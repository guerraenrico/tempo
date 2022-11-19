package com.enricog.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoDialogAlert(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    positiveAction: TempoDialogAction,
    negativeAction: TempoDialogAction?,
    onDismiss: () -> Unit,
    isCancellable: Boolean,
) {
    TempoDialogBase(onDismiss = onDismiss, isCancellable = isCancellable) {
        Column(
            modifier = modifier
                .fillMaxWidth(0.8f)
                .background(
                    color = TempoTheme.colors.background,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = TempoTheme.typography.h1,
                    color = TempoTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = description,
                    style = TempoTheme.typography.body1,
                    color = TempoTheme.colors.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (negativeAction != null) {
                    TempoButton(
                        onClick = negativeAction.onClick,
                        text = negativeAction.text,
                        contentDescription = negativeAction.contentDescription
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
                TempoButton(
                    onClick = positiveAction.onClick,
                    text = positiveAction.text,
                    contentDescription = positiveAction.contentDescription
                )
            }
        }
    }
}

@Composable
private fun TempoDialogBase(
    onDismiss: () -> Unit,
    isCancellable: Boolean,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = isCancellable,
            dismissOnClickOutside = isCancellable,
            usePlatformDefaultWidth = false
        ),
        content = content
    )
}

@Preview
@Composable
private fun Preview() {
    TempoDialogAlert(
        title = "title",
        description = "description",
        positiveAction = TempoDialogAction(
            text = "positive",
            contentDescription = "positive",
            onClick = {}
        ),
        negativeAction = TempoDialogAction(
            text = "negative",
            contentDescription = "negative",
            onClick = {}
        ),
        onDismiss = { },
        isCancellable = false
    )
}
