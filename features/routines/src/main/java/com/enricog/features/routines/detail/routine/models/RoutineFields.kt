package com.enricog.features.routines.detail.routine.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.ui.components.textField.TimeText

internal data class RoutineFields(
    val name: TextFieldValue,
    val startTimeOffset: TimeText
)