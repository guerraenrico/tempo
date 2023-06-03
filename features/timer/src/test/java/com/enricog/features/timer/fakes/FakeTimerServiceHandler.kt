package com.enricog.features.timer.fakes

import com.enricog.features.timer.service.TimerServiceHandler
import com.google.common.truth.Truth.assertThat

internal class FakeTimerServiceHandler : TimerServiceHandler {

    private var isServiceStarted: Boolean = false

    override fun start() {
        isServiceStarted = true
    }

    override fun stop() {
        isServiceStarted = false
    }

    fun assertServiceIsStarted() {
        assertThat(isServiceStarted).isTrue()
    }

    fun assertServiceIsNotStarted() {
        assertThat(isServiceStarted).isFalse()
    }
}