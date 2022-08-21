package com.enricog.tempo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.logger.api.TempoLogger
import com.enricog.features.routines.RoutinesNavigation
import com.enricog.features.timer.TimerNavigation
import com.enricog.features.timer.WindowScreenManager
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.Navigator
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.black
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
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
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                val navController = rememberNavController(bottomSheetNavigator)

                DisposableEffect(key1 = true) {
                    val listener = OnDestinationChangedListener { _, destination, _ ->
                        destination.route?.let { TempoLogger.setRouteName(it) }
                    }
                    navController.addOnDestinationChangedListener(listener)
                    onDispose { navController.removeOnDestinationChangedListener(listener) }
                }

                LaunchedEffect(key1 = true) {
                    navigator.actions.collect { action ->
                        when (action) {
                            NavigationAction.GoBack -> {
                                navController.popBackStack()
                            }
                            is NavigationAction.GoTo -> {
                                navController.navigate(action.route, action.navOptions)
                            }
                        }
                    }
                }

                Scaffold() {
                }
                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    sheetShape = TempoTheme.shapes.bottomSheet,
                    sheetBackgroundColor = TempoTheme.colors.background,
                    sheetContentColor = TempoTheme.colors.onBackground,
                    scrimColor = black.copy(alpha = 0.60f)
                ) {
                    NavHost(navController = navController, startDestination = "routinesNav") {
                        RoutinesNavigation()
                        TimerNavigation()
                    }
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
