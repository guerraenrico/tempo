package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.enricog.routines.RoutinesNavigation
import com.enricog.timer.TimerNavigation
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContent {
            TempoTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "routinesNav") {
                    RoutinesNavigation(navController)
                    TimerNavigation(navController)
                }
            }
        }
    }
}