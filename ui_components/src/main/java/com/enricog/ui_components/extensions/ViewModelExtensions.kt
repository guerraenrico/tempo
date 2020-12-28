package com.enricog.ui_components.extensions

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.lifecycle.HiltViewModelFactory
import androidx.hilt.lifecycle.ViewModelAssistedFactory
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

val AmbientViewModelFactory = staticAmbientOf<ViewModelProvider.Factory>()

@Composable
inline fun <reified VM : ViewModel> viewModel(): VM = viewModel(
    modelClass = VM::class.java,
    key = null,
    factory = AmbientViewModelFactory.current
)

// FIXME: 😱 remove when https://github.com/google/dagger/issues/2166 is solved should use above function

val AmbientNavController = staticAmbientOf<NavController>()
val AmbientApplication = staticAmbientOf<Application>()
val AmbientViewModelFactoriesMap =
    staticAmbientOf<Map<String, ViewModelAssistedFactory<out ViewModel>>>()

@Composable
inline fun <reified VM : ViewModel> navViewModel(): VM {
    val navController = AmbientNavController.current
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val factory = AmbientViewModelFactory.current
    return if (backStackEntry != null) {
        // Hack for navigation viewModel
        val application = AmbientApplication.current
        val viewModelFactories = AmbientViewModelFactoriesMap.current
        val delegate = SavedStateViewModelFactory(application, backStackEntry, null)
        val constructor = HiltViewModelFactory::class.java.declaredConstructors.first().apply {
            isAccessible = true
        }
        val hiltViewModelFactory = constructor.newInstance(backStackEntry, null, delegate, viewModelFactories) as HiltViewModelFactory
        viewModel(null, hiltViewModelFactory)
    } else {
        viewModel(null, factory)
    }
}


@Composable
fun ProvideNavigationViewModelFactoryMap(
    factory: HiltViewModelFactory,
    content: @Composable () -> Unit
) {
    // Hack for navigation viewModel
    @Suppress("UNCHECKED_CAST")
    val factories =
        HiltViewModelFactory::class.java.getDeclaredField("mViewModelFactories")
            .also { it.isAccessible = true }
            .get(factory).let {
                it as Map<String, ViewModelAssistedFactory<out ViewModel>>
            }
    Providers(
        AmbientViewModelFactoriesMap provides factories
    ) {
        content.invoke()
    }
}