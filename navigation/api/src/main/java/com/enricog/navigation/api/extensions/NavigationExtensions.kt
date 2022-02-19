package com.enricog.navigation.api.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified VM : ViewModel> navViewModel(navBackStackEntry: NavBackStackEntry): VM {
    return viewModel(
        factory = HiltViewModelFactory(
            context = LocalContext.current,
            navBackStackEntry = navBackStackEntry
        )
    )
}
