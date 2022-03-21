package com.enricog.features.routines.detail.ui.time_type

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.darkBlue500
import com.enricog.ui.theme.white

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"
private val chipShape = RoundedCornerShape(percent = 50)

@Composable
internal fun TimeTypeChip(
    value: TimeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: ((TimeType) -> Unit)? = null
) {
    Box(
        modifier = modifier
            .testTag(TimeTypeChipTestTag)
            .alpha(if (isSelected) 1f else 0.7f)
            .clip(chipShape)
            .background(color = value.color(isSelected), shape = chipShape)
            .clickable(enabled = onClick != null) {
                if (!isSelected) {
                    onClick?.invoke(value)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.text(),
            color = white,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}

private fun TimeType.color(isSelected: Boolean): Color {
    if (!isSelected) return darkBlue500
    return color()
}
