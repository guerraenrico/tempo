package com.enricog.base_android.viewmodel

interface StateConverter<ViewModelState: Any, ViewState: Any> {
    suspend fun convert(state: ViewModelState): ViewState
}