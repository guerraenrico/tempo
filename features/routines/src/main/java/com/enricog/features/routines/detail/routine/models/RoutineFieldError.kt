package com.enricog.features.routines.detail.routine.models

import androidx.annotation.StringRes
import com.enricog.features.routines.R

internal enum class RoutineFieldError(@StringRes val stringResId: Int) {
    BlankRoutineName(stringResId = R.string.field_error_message_routine_name_blank)
}
