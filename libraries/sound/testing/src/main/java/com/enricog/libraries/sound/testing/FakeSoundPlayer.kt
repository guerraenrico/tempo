package com.enricog.libraries.sound.testing

import com.enricog.libraries.sound.api.SoundPlayer
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FakeSoundPlayer : SoundPlayer {

    private val playedSounds = mutableMapOf<Int, Int>()

    override fun play(soundResId: Int) {
        val count = playedSounds.putIfAbsent(soundResId, 0) ?: 0
        playedSounds[soundResId] = count + 1
    }

    override fun release(soundResId: Int) {
        playedSounds.remove(soundResId)
    }

    override fun close() {
        playedSounds.clear()
    }

    fun assertSoundPlayed(soundResId: Int, times: Int) {
        assertContains(playedSounds, soundResId)
        assertEquals(times, playedSounds[soundResId])
    }

    fun assertSoundNotPlayed(soundResId: Int) {
        assertFalse("Expected the collection to contain the element.\nCollection <$playedSounds>, element <$soundResId>.") {
            playedSounds.contains(soundResId)
        }
    }
}