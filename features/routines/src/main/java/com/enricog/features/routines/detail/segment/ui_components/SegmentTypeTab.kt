package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.ui.time_type.text
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.white

internal const val SegmentTypeTabTestTag = "SegmentTypeTabTestTag"

@Composable
internal fun SegmentTypeTab(
    value: TimeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: ((TimeType) -> Unit)? = null
) {
    Box(
        modifier = modifier
            .testTag(SegmentTypeTabTestTag)
            .clip(shape = RoundedCornerShape(percent = 50))
            .clickable(enabled = onClick != null) {
                if (!isSelected) {
                    onClick?.invoke(value)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        TempoText(
            text = AnnotatedString(value.text()),
            style = TextStyle(
                color = white,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            ),
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}