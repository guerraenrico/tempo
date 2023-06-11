package com.enricog.libraries.sound.testing

import com.enricog.libraries.sound.api.SoundPlayer
import com.google.common.truth.Truth.assertThat

class FakeSoundPlayer : SoundPlayer {

    private val playedSounds = mutableMapOf<Int, Int>()

    override fun play(soundResId: Int) {
        val count = playedSounds.putIfAbsent(soundResId, 0) ?: 0
        playedSounds[soundResId] = count + 1
    }

    override fun keepAlive(soundResId: Int) {
        // no-op
    }

    override fun close() {
        playedSounds.clear()
    }

    fun assertSoundPlayed(soundResId: Int, times: Int = 1) {
        require(times >= 1)
        assertThat(playedSounds).containsEntry(soundResId, times)
    }

    fun assertSoundNotPlayed(soundResId: Int) {
        assertThat(playedSounds).doesNotContainKey(soundResId)
    }

    fun assertNoSoundHasPlayed() {
        assertThat(playedSounds).isEmpty()
    }
}