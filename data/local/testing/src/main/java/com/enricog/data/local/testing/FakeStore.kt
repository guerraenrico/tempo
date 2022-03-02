package com.enricog.data.local.testing

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeStore<T : Any>(initialValue: T) {

    private val store = MutableStateFlow(initialValue)

    fun update(block: (T) -> T) {
        store.update(block)
    }

    fun put(value: T) {
        store.value = value
    }

    fun get(): T {
        return store.value
    }

    fun asFlow(): Flow<T> {
        return store.asStateFlow()
    }
}