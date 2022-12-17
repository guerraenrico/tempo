package com.enricog.tempo.managers

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnErrorListener
import androidx.annotation.RawRes
import com.enricog.core.logger.api.TempoLogger
import com.enricog.features.timer.SoundPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

internal class SoundPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SoundPlayer, OnErrorListener {

    private val players = ConcurrentHashMap<Int, MediaPlayer>()

    override fun play(soundResId: Int) {
        val player = createPlayerIfNeeded(soundResId = soundResId)
        player.start()
    }

    private fun createPlayerIfNeeded(@RawRes soundResId: Int): MediaPlayer {
        return players.getOrPut(soundResId) {
            MediaPlayer.create(context, soundResId).apply {
                isLooping = false
                setOnErrorListener(this@SoundPlayerImpl)
            }
        }
    }

    override fun release(soundResId: Int) {
        releasePlayer(soundResId = soundResId)
    }

    override fun close() {
        players.keys.forEach(::releasePlayer)
    }

    private fun releasePlayer(@RawRes soundResId: Int) {
        players[soundResId]?.release()
        players.remove(soundResId)
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TempoLogger.e(IllegalStateException("MediaPlayer error -> what: $what, extra: $extra"))
        return false
    }
}
