package com.enricog.ui.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoSnackbar(
    snackbarData: TempoSnackbarData,
    modifier: Modifier
) {
    val actionText = snackbarData.actionText
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = TempoSnackbarDefaults.paddingHorizontal,
                vertical = TempoSnackbarDefaults.paddingVertical
            )
            .clip(TempoTheme.shapes.medium)
            .background(TempoSnackbarDefaults.backgroundColor)
    ) {
        Row(
            modifier = modifier.padding(
                horizontal = TempoSnackbarDefaults.paddingHorizontal,
                vertical = TempoSnackbarDefaults.paddingVertical
            )
        ) {
            TempoText(
                text = snackbarData.message,
                style = TempoTheme.typography.body1.copy(
                    color = TempoTheme.colors.surface
                )
            )
            if (actionText != null) {
                TempoButton(
                    onClick = { snackbarData.perform() },
                    text = actionText,
                    contentDescription = actionText
                )
            }
        }
    }
}

private object TempoSnackbarDefaults {

    val backgroundColor: Color
        @Composable get() = TempoTheme.colors.onSurfaceSecondary.copy(alpha = 0.95f)

    val paddingVertical: Dp
        @Composable get() = TempoTheme.dimensions.spaceS

    val paddingHorizontal: Dp
        @Composable get() = TempoTheme.dimensions.spaceS
}
