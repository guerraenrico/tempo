package com.enricog.ui_components.ambients

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified VM : ViewModel> navViewModel(navBackStackEntry: NavBackStackEntry): VM {
    return viewModel(
        factory = HiltViewModelFactory(
            AmbientContext.current,
            navBackStackEntry
        )
    )
}