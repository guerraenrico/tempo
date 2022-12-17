package com.enricog.libraries.sound.testing

import com.enricog.libraries.sound.api.SoundPlayer
import kotlin.test.assertContains
import kotlin.test.assertFalse

class FakeSoundPlayer : SoundPlayer {

    private val fakePlayers = mutableSetOf<Int>()

    override fun play(soundResId: Int) {
        fakePlayers.add(soundResId)
    }

    override fun release(soundResId: Int) {
        fakePlayers.remove(soundResId)
    }

    override fun close() {
        fakePlayers.clear()
    }

    fun assertSoundPlayed(soundResId: Int) {
        assertContains(fakePlayers, soundResId)
    }

    fun assertSoundNotPlayed(soundResId: Int) {
        assertFalse("Expected the collection to contain the element.\nCollection <$fakePlayers>, element <$soundResId>.") {
            fakePlayers.contains(soundResId)
        }
    }
}