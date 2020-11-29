package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.enricog.routines.RoutinesList
import com.enricog.ui_components.resources.TempoTheme

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      TempoTheme {
        RoutinesList()
      }
    }
  }
}