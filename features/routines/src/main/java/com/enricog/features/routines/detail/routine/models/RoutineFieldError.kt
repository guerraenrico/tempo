package com.enricog.features.routines.detail.routine.models

import androidx.annotation.StringRes
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R

internal enum class RoutineFieldError(@StringRes val stringResId: Int, val formatArgs: Array<Any>) {
    BlankRoutineName(
        stringResId = R.string.field_error_message_routine_name_blank,
        formatArgs = arrayOf()
    ),
    BlankRoutineRounds(
        stringResId = R.string.field_error_message_routine_rounds_blank,
        formatArgs = arrayOf(Routine.MIN_NUM_ROUNDS)
    )
}
