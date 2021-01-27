package com.enricog.ui_components.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AndroidDialogProperties
import androidx.compose.ui.window.Dialog
import com.enricog.ui_components.common.button.TempoButton


@Composable
fun TempoDialogAlert(
    title: String,
    description: String,
    positiveAction: TempoDialogAction,
    negativeAction: TempoDialogAction?,
    onDismiss: () -> Unit,
    isCancellable: Boolean,
) {
    TempoDialogBase(onDismiss = onDismiss, isCancellable = isCancellable) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(
                    color = MaterialTheme.colors.background,
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
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onSurface
                )
                Spacer(modifier = Modifier.preferredHeight(10.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (negativeAction != null) {
                    TempoButton(onClick = negativeAction.onClick, text = negativeAction.text)
                    Spacer(modifier = Modifier.preferredWidth(16.dp))
                }
                TempoButton(onClick = positiveAction.onClick, text = positiveAction.text)
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
        properties = AndroidDialogProperties(
            dismissOnBackPress = isCancellable,
            dismissOnClickOutside = isCancellable
        ),
        content = content
    )
}