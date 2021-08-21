package com.enricog.routines.detail.routine.models

internal sealed class RoutineFieldError {
    object BlankRoutineName : RoutineFieldError()
}
