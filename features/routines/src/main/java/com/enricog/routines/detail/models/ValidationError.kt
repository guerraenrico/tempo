package com.enricog.routines.detail.models

internal sealed class ValidationError {
    object BlankRoutineName: ValidationError()

    object BlankSegmentName: ValidationError()
}