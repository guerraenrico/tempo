package com.enricog.ui_components.ambients

import android.app.Activity
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableContract
import androidx.compose.runtime.Providers
import androidx.hilt.lifecycle.HiltViewModelFactory
import androidx.hilt.lifecycle.ViewModelAssistedFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController

// FIXME: 😱 remove when https://github.com/google/dagger/issues/2166 remove reference to navController, hiltViewModelFactory and the ugly reflection thing

object TempoAmbient {

    val viewModelFactory: ViewModelProvider.Factory
        @Composable
        @ComposableContract(readonly = true)
        get() = AmbientViewModelFactory.current

    val activity: Activity
        @Composable
        @ComposableContract(readonly = true)
        get() = AmbientActivity.current

    val application: Application
        @Composable
        @ComposableContract(readonly = true)
        get() = AmbientApplication.current

    val navController: NavController
        @Composable
        @ComposableContract(readonly = true)
        get() = AmbientNavController.current

    val factoriesMap: Map<String, ViewModelAssistedFactory<out ViewModel>>
        @Composable
        @ComposableContract(readonly = true)
        get() = AmbientViewModelFactoriesMap.current

}


@Composable
fun ProvideTempoAmbient(
    viewModelFactory: ViewModelProvider.Factory,
    activity: Activity,
    application: Application,
    navController: NavController,
    content: @Composable () -> Unit
) {
    // Hack for navigation viewModel
    val factory = viewModelFactory as HiltViewModelFactory

    @Suppress("UNCHECKED_CAST")
    val factories =
        HiltViewModelFactory::class.java.getDeclaredField("mViewModelFactories")
            .also { it.isAccessible = true }
            .get(factory).let {
                it as Map<String, ViewModelAssistedFactory<out ViewModel>>
            }
    Providers(
        AmbientViewModelFactory provides viewModelFactory,
        AmbientActivity provides activity,
        AmbientApplication provides application,
        AmbientNavController provides navController,
        AmbientViewModelFactoriesMap provides factories,
        content = content
    )
}