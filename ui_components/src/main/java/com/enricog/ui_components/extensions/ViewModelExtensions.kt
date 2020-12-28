package com.enricog.ui_components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

val AmbientViewModelFactory = staticAmbientOf<ViewModelProvider.Factory>()

@Composable
inline fun <reified VM : ViewModel> viewModel(): VM = viewModel(
    modelClass = VM::class.java,
    key = null,
    factory = AmbientViewModelFactory.current
)