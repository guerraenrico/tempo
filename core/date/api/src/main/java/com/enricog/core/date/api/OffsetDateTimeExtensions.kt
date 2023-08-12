package com.enricog.core.date.api

import java.time.OffsetDateTime
import java.time.ZoneOffset

fun OffsetDateTime.shiftToStartOfDay(): OffsetDateTime {
    return OffsetDateTime.of(
        /* year = */ year,
        /* month = */ monthValue,
        /* dayOfMonth = */ dayOfMonth,
        /* hour = */ 0,
        /* minute = */ 0,
        /* second = */ 0,
        /* nanoOfSecond = */ 0,
        /* offset = */ ZoneOffset.UTC
    )
}

fun OffsetDateTime.shiftToEndOfDay(): OffsetDateTime {
    return OffsetDateTime.of(
        /* year = */ year,
        /* month = */ monthValue,
        /* dayOfMonth = */ dayOfMonth,
        /* hour = */ 23,
        /* minute = */ 59,
        /* second = */ 59,
        /* nanoOfSecond = */ 0,
        /* offset = */ ZoneOffset.UTC
    )
}