package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.entities.routines.TimeType
import com.enricog.routines.R
import com.enricog.ui_components.resources.*

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"

@Composable
internal fun TimeTypeChip(
    value: TimeType,
    isSelected: Boolean,
    onSelect: (TimeType) -> Unit
) {
    val chipShape = RoundedCornerShape(5.dp)
    Text(
        text = value.description,
        color = white,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        modifier = Modifier
            .testTag(TimeTypeChipTestTag)
            .padding(MaterialTheme.dimensions.spaceS)
            .background(color = value.color, shape = chipShape)
            .border(width = 2.dp, color = if (isSelected) white else Color.Transparent, chipShape)
            .alpha(if (isSelected) 1f else 0.7f)
            .clickable {
                if (!isSelected) {
                    onSelect(value)
                }
            }
    )
}

private val TimeType.color: Color
    get() {
        return when (this) {
            TimeType.TIMER -> blue500
            TimeType.REST -> purple500
            TimeType.STOPWATCH -> darkBlue500
        }
    }

@Composable
private val TimeType.description: String
    get() = when (this) {
        TimeType.REST -> stringResource(R.string.chip_time_type_rest_name)
        TimeType.TIMER -> stringResource(R.string.chip_time_type_timer_name)
        TimeType.STOPWATCH -> stringResource(R.string.chip_time_type_stopwatch_name)
    }