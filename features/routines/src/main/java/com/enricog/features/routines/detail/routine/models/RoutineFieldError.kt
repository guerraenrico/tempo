package com.enricog.features.routines.detail.routine.models

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R

internal enum class RoutineFieldError(@StringRes val stringResId: Int, val formatArgs: ImmutableList<Any>) {
    BlankRoutineName(
        stringResId = R.string.field_error_message_routine_name_blank,
        formatArgs = immutableListOf()
    ),
    BlankRoutineRounds(
        stringResId = R.string.field_error_message_routine_rounds_blank,
        formatArgs = immutableListOf(Routine.MIN_NUM_ROUNDS)
    )
}
