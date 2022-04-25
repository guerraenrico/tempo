package com.enricog.features.routines.detail.routine.models

import com.enricog.ui.components.textField.TimeText

internal data class RoutineFields(
    val name: String,
    val startTimeOffset: TimeText
)