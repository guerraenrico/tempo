package com.enricog.features.routines.detail.routine.models

internal sealed class RoutineFieldError {
    object BlankRoutineName : RoutineFieldError()
}
