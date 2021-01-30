package com.enricog.tempo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.onDispose
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.coroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.routines.RoutinesNavigation
import com.enricog.tempo.navigation.Navigator
import com.enricog.timer.TimerNavigation
import com.enricog.timer.WindowScreenManager
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var windowScreenManager: WindowScreenManager

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempoTheme {
                val navController = rememberNavController()
                navigator.navController = navController

                NavHost(navController = navController, startDestination = "routinesNav") {
                    RoutinesNavigation()
                    TimerNavigation()
                }

                onDispose { navigator.navController = null }
            }
        }

        windowScreenManager.keepScreenOn
            .onEach(::toggleKeepScreenOnFlag)
            .flowOn(dispatchers.main)
            .launchIn(lifecycle.coroutineScope)
    }

    private fun toggleKeepScreenOnFlag(enable: Boolean) {
        val flag = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        if (enable) {
            window.addFlags(flag)
        } else {
            window.clearFlags(flag)
        }
    }
}