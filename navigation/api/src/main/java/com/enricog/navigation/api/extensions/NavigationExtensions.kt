package com.enricog.navigation.api.extensions

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry

@Composable
inline fun <reified VM : ViewModel> navViewModel(navBackStackEntry: NavBackStackEntry): VM {
    return hiltViewModel(navBackStackEntry)
}
