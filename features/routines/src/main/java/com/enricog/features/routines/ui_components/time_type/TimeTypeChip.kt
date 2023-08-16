package com.enricog.features.routines.ui_components.time_type

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.core.compose.api.classes.Text
import com.enricog.ui.components.chip.TempoChip
import com.enricog.ui.components.chip.TempoChipColor

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"

@Composable
internal fun TimeTypeChip(
    style: TimeTypeStyle,
    modifier: Modifier = Modifier
) {
    TempoChip(
        modifier = modifier
            .testTag(TimeTypeChipTestTag),
        text = Text.Resource(resId = style.nameStringResId),
        color = TempoChipColor.Adaptive(
            onBackgroundColor = style.onBackgroundColor,
            backgroundColor = style.backgroundColor
        )
    )
}
