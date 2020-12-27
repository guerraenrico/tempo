package com.enricog.ui_components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

val AmbientViewModelFactory = staticAmbientOf<ViewModelProvider.Factory?> { null }

@Composable
inline fun <reified VM : ViewModel> viewModel(
    key: String? = null,
): VM = viewModel(VM::class.java, key, AmbientViewModelFactory.current)