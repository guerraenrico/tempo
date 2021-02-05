package com.enricog.routines.detail.routine.models

internal sealed class RoutineField {
    object Name : RoutineField()
    object StartTimeOffsetInSeconds : RoutineField()
}
