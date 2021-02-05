package com.enricog.routines.detail.segment.models

internal sealed class SegmentField {
    object Name : SegmentField()
    object TimeInSeconds : SegmentField()
}
