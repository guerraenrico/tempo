package com.enricog.tempo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.extensions.exhaustive
import com.enricog.navigation.NavigationAction
import com.enricog.navigation.Navigator
import com.enricog.routines.RoutinesNavigation
import com.enricog.timer.TimerNavigation
import com.enricog.timer.WindowScreenManager
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

                LaunchedEffect(key1 = true) {
                    navigator.actions.collect { action ->
                        when (action) {
                            NavigationAction.GoBack -> {
                                navController.popBackStack()
                                Unit
                            }
                            is NavigationAction.GoTo -> {
                                navController.navigate(action.route, action.navOptions)
                            }
                        }.exhaustive
                    }
                }

                NavHost(navController = navController, startDestination = "routinesNav") {
                    RoutinesNavigation()
                    TimerNavigation()
                }
            }
        }

        windowScreenManager.keepScreenOn
            .onEach { toggleKeepScreenOnFlag(it) }
            .flowOn(dispatchers.main)
            .launchIn(lifecycleScope)
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
