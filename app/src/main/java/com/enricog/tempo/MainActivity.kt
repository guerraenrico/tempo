package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.setContent
import androidx.hilt.lifecycle.HiltViewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.routines.RoutinesNavigation
import com.enricog.tempo.navigation.Navigator
import com.enricog.timer.TimerNavigation
import com.enricog.ui_components.extensions.AmbientApplication
import com.enricog.ui_components.extensions.AmbientNavController
import com.enricog.ui_components.extensions.ProvideNavigationViewModelFactoryMap
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContent {
            TempoTheme(defaultViewModelProviderFactory) {
                val navController = rememberNavController()
                navigator.navController = navController

                Providers(
                    AmbientApplication provides application,
                    AmbientNavController provides navController
                ) {
                    ProvideNavigationViewModelFactoryMap(factory = defaultViewModelProviderFactory as HiltViewModelFactory) {

                        NavHost(navController = navController, startDestination = "routinesNav") {
                            RoutinesNavigation()
                            TimerNavigation()
                        }

                    }
                }


            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navigator.navController = null
    }
}