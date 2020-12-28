package com.enricog.ui_components.ambients

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.lifecycle.HiltViewModelFactory
import androidx.hilt.lifecycle.ViewModelAssistedFactory
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

internal val AmbientViewModelFactory = staticAmbientOf<ViewModelProvider.Factory>()

@Composable
inline fun <reified VM : ViewModel> viewModel(): VM = viewModel(
    modelClass = VM::class.java,
    key = null,
    factory = TempoAmbient.viewModelFactory
)

// FIXME: 😱 remove when https://github.com/google/dagger/issues/2166 is solved should use above function

internal val AmbientNavController = staticAmbientOf<NavController>()
internal val AmbientApplication = staticAmbientOf<Application>()
internal val AmbientViewModelFactoriesMap =
    staticAmbientOf<Map<String, ViewModelAssistedFactory<out ViewModel>>>()

@Composable
inline fun <reified VM : ViewModel> navViewModel(): VM {
    val navController = TempoAmbient.navController
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val factory = TempoAmbient.viewModelFactory
    return if (backStackEntry != null) {
        // Hack for navigation viewModel
        val application = TempoAmbient.application
        val viewModelFactories = TempoAmbient.factoriesMap
        val delegate = SavedStateViewModelFactory(application, backStackEntry, null)
        val constructor = HiltViewModelFactory::class.java.declaredConstructors.first().apply {
            isAccessible = true
        }
        val hiltViewModelFactory = constructor.newInstance(
            backStackEntry,
            null,
            delegate,
            viewModelFactories
        ) as HiltViewModelFactory
        viewModel(null, hiltViewModelFactory)
    } else {
        viewModel(null, factory)
    }
}