package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.routines.RoutinesNavigation
import com.enricog.tempo.navigation.Navigator
import com.enricog.timer.TimerNavigation
import com.enricog.ui_components.ambients.ProvideTempoAmbient
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempoTheme {
                val navController = rememberNavController()
                navigator.navController = navController

                ProvideTempoAmbient(
                    viewModelFactory = defaultViewModelProviderFactory,
                    activity = this,
                    application = application,
                    navController = navController
                ) {

                    NavHost(navController = navController, startDestination = "routinesNav") {
                        RoutinesNavigation()
                        TimerNavigation()
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