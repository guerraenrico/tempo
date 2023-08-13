package com.enricog.features.routines.ui_components.time_type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"
private val chipShape = RoundedCornerShape(percent = 50)

@Composable
internal fun TimeTypeChip(
    style: TimeTypeStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag(TimeTypeChipTestTag)
            .clip(chipShape)
            .background(color = style.backgroundColor, shape = chipShape),
        contentAlignment = Alignment.Center
    ) {
        TempoText(
            text = stringResource(id = style.nameStringResId),
            style = TempoTheme.typography.h4.copy(
                color = style.onBackgroundColor
            ),
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}
