package com.enricog.routines.detail.models

internal sealed class Field {

    sealed class Routine : Field() {
        object Name : Routine()
        object StartTimeOffsetInSeconds : Routine()
        object Segments : Routine()
    }

    sealed class Segment : Field() {
        object Name : Segment()
        object TimeInSeconds : Segment()
    }
}