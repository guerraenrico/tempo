package com.enricog.core.coroutine.job

import kotlinx.coroutines.Job
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class AutoCancelableJob : ReadWriteProperty<Any, Job?> {

    private var job: Job? = null

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Job?) {
        job?.cancel()
        job = value
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Job? {
        return job
    }
}

fun autoCancelableJob(): ReadWriteProperty<Any, Job?> {
    return AutoCancelableJob()
}