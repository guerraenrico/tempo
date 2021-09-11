package com.enricog.timer_service.controller

import com.enricog.entities.routines.Routine
import com.enricog.timer_service.models.TimerState
import kotlinx.coroutines.flow.Flow

sealed interface TimerController {

    val state: Flow<TimerState>

    fun start(routine: Routine)

    fun stop()

    fun toggleTimeRunning()

    fun restart()
}