package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.entities.routines.TimeType
import com.enricog.routines.R
import com.enricog.ui_components.resources.TimeTypeColors
import com.enricog.ui_components.resources.darkBlue500
import com.enricog.ui_components.resources.dimensions
import com.enricog.ui_components.resources.white

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"

@Composable
internal fun TimeTypeChip(
    value: TimeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: ((TimeType) -> Unit)? = null
) {
    val chipShape = RoundedCornerShape(percent = 50)
    Box(
        modifier = modifier
            .testTag(TimeTypeChipTestTag)
            .padding(horizontal = MaterialTheme.dimensions.spaceS)
            .alpha(if (isSelected) 1f else 0.7f)
            .clip(chipShape)
            .background(color = value.color(isSelected), shape = chipShape)
            .border(width = 1.dp, color = if (isSelected) white else Color.Transparent, chipShape)
            .clickable {
                if (!isSelected) {
                    onSelect?.invoke(value)
                }
            }
    ) {
        Text(
            text = value.description,
            color = white,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimensions.spaceS,
                vertical = MaterialTheme.dimensions.spaceXS
            )
        )
    }

}

private fun TimeType.color(isSelected: Boolean): Color {
    if (!isSelected) return darkBlue500

    return when (this) {
        TimeType.TIMER -> TimeTypeColors.TIMER
        TimeType.REST -> TimeTypeColors.REST
        TimeType.STOPWATCH -> TimeTypeColors.STOPWATCH
    }
}

@Composable
private val TimeType.description: String
    get() = when (this) {
        TimeType.REST -> stringResource(R.string.chip_time_type_rest_name)
        TimeType.TIMER -> stringResource(R.string.chip_time_type_timer_name)
        TimeType.STOPWATCH -> stringResource(R.string.chip_time_type_stopwatch_name)
    }