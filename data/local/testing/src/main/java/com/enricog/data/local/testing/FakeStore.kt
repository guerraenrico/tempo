package com.enricog.data.local.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update

class FakeStore<T : Any>(initialValue: T) {

    private val store = MutableStateFlow(initialValue)
    private var isErrorEnabled: Boolean = false

    fun update(block: (T) -> T) {
        throwErrorIfEnabled()
        store.update(block)
    }

    fun put(value: T) {
        throwErrorIfEnabled()
        store.value = value
    }

    fun get(): T {
        throwErrorIfEnabled()
        return store.value
    }

    fun asFlow(): Flow<T> {
        return store.asStateFlow()
            .onEach { throwErrorIfEnabled() }
    }

    fun enableErrorOnNextAccess() {
        isErrorEnabled = true
    }

    private fun throwErrorIfEnabled() {
        if (isErrorEnabled) {
            isErrorEnabled = false
            throw IllegalAccessException("Store error")
        }
    }
}