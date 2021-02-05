package com.enricog.routines.detail.segment.models

internal sealed class SegmentFieldError {
    object BlankSegmentName : SegmentFieldError()
    object InvalidSegmentTime : SegmentFieldError()
}
