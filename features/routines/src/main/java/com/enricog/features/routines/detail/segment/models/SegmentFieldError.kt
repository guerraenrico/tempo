package com.enricog.features.routines.detail.segment.models

internal sealed class SegmentFieldError {
    object BlankSegmentName : SegmentFieldError()
    object InvalidSegmentTime : SegmentFieldError()
}
