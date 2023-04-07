package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.contentColorFor

internal const val SegmentTypeTabTestTag = "SegmentTypeTabTestTag"

@Composable
internal fun SegmentTypeTab(
    timeTypeStyle: TimeTypeStyle,
    progress: Float,
    onClick: (TimeTypeStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textColor = lerp(
        start = timeTypeStyle.onBackgroundColor,
        stop = TempoTheme.colors.contentColorFor(
            backgroundColor = TempoTheme.colors.background
        ),
        fraction = progress
    )
    Box(
        modifier = modifier
            .testTag(SegmentTypeTabTestTag)
            .clip(shape = RoundedCornerShape(percent = 50))
            .clickable { onClick(timeTypeStyle) },
        contentAlignment = Alignment.Center
    ) {
        TempoText(
            text = stringResource(id = timeTypeStyle.nameStringResId),
            style = TempoTheme.typography.h4.copy(
                color = textColor
            ),
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}
