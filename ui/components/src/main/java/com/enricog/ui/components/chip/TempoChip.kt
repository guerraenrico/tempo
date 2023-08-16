package com.enricog.ui.components.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.enricog.core.compose.api.classes.Text
import com.enricog.core.compose.api.modifiers.semantics.innerText
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoChip(
    text: Text,
    modifier: Modifier = Modifier,
    color: TempoChipColor = TempoChipColor.Default
) {
    val resolvedText = text.resolveString()
    Box(
        modifier = modifier
            .semantics {
                innerText(value = resolvedText)
            }
            .clip(TempoChipDefaults.shape)
            .background(color = color.backgroundColor, shape = TempoChipDefaults.shape),
        contentAlignment = Alignment.Center
    ) {
        TempoText(
            text = resolvedText,
            style = TempoTheme.typography.h4.copy(
                color = color.onBackgroundColor
            ),
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}

private object TempoChipDefaults {

    val shape = RoundedCornerShape(percent = 50)
}

@Preview
@Composable
private fun Preview() {
    TempoChip(text = Text.String(value = "Text"))
}
