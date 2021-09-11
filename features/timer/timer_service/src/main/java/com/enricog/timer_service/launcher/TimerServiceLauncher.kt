package com.enricog.timer_service.launcher

sealed interface TimerServiceLauncher {
    fun launchWorker()
    fun launchService()
}