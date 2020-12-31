package com.enricog.routines.detail.models

internal sealed class ValidationError {
    object BlankRoutineName: ValidationError()
    object InvalidRoutineStartTimeOffset: ValidationError()
    object NoSegmentsInRoutine: ValidationError()

    object BlankSegmentName: ValidationError()
    object InvalidSegmentTime: ValidationError()
}