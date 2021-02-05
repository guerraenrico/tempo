package com.enricog.core.coroutine.job

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.Job

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
