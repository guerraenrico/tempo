package com.enricog.base.viewmodel

interface StateConverter<ViewModelState : Any, ViewState : Any> {
    suspend fun convert(state: ViewModelState): ViewState
}
