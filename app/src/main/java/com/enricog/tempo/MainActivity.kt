package com.enricog.tempo

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.base.extensions.exhaustive
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.features.routines.RoutinesNavigation
import com.enricog.features.timer.TimerNavigation
import com.enricog.features.timer.WindowScreenManager
import com.enricog.navigation.api.NavigationAction
import com.enricog.navigation.api.Navigator
import com.enricog.ui.theme.TempoTheme
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

                ModalBottomSheetLayout(
                    bottomSheetNavigator = bottomSheetNavigator,
                    sheetShape = TempoTheme.shapes.bottomSheet,
                    sheetBackgroundColor = TempoTheme.colors.surface,
                    sheetContentColor = TempoTheme.colors.onSurface,
                    scrimColor = TempoTheme.colors.surface.copy(alpha = 0.60f)
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
