package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.setContent
import com.enricog.routines.RoutinesList
import com.enricog.ui_components.resources.TempoTheme
import com.enricog.timer.Timer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempoTheme {
                Column {
                    RoutinesList()
                    Timer()
                }
            }
        }
    }
}