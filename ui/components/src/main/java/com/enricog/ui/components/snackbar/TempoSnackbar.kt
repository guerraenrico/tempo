package com.enricog.ui.components.snackbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import java.util.Locale

@Composable
fun TempoSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: TempoSnackbarData
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
            modifier = modifier
                .padding(
                    horizontal = TempoSnackbarDefaults.paddingHorizontal,
                    vertical = TempoSnackbarDefaults.paddingVertical
                )
        ) {
            TempoSnackbarText(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = snackbarData.message
            )
            if (actionText != null) {
                Spacer(modifier = Modifier.width(TempoSnackbarDefaults.margin))
                TempoSnackbarAction(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable { snackbarData.perform() },
                    text = actionText.uppercase(Locale.getDefault())
                )
            }
        }
    }
}

@Composable
private fun TempoSnackbarText(modifier: Modifier = Modifier, text: String) {
    TempoText(
        modifier = modifier,
        text = text,
        style = TempoTheme.typography.body1.copy(
            color = TempoSnackbarDefaults.contentColor
        )
    )
}

@Composable
private fun TempoSnackbarAction(modifier: Modifier = Modifier, text: String) {
    TempoText(
        modifier = modifier.padding(TempoTheme.dimensions.spaceS),
        text = text.uppercase(Locale.getDefault()),
        style = TempoTheme.typography.button.copy(
            color = TempoSnackbarDefaults.contentColor
        )
    )
}

private object TempoSnackbarDefaults {

    val backgroundColor: Color
        @Composable get() = TempoTheme.colors.onSurfaceSecondary.copy(alpha = 0.97f)

    val contentColor: Color
        @Composable get() = TempoTheme.colors.surface

    val paddingVertical: Dp
        @Composable get() = TempoTheme.dimensions.spaceS

    val paddingHorizontal: Dp
        @Composable get() = TempoTheme.dimensions.spaceM

    val margin: Dp
        @Composable get() = TempoTheme.dimensions.spaceS
}
