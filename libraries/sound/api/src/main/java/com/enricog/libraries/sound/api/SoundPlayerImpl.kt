package com.enricog.libraries.sound.api

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnErrorListener
import androidx.annotation.RawRes
import com.enricog.core.logger.api.TempoLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class SoundPlayerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SoundPlayer, OnErrorListener {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val audioSessionId = audioManager.generateAudioSessionId()
    private var lastSoundResId = -1
    private var player: MediaPlayer? = null

    override fun play(soundResId: Int) {
        createPlayerIfNeeded(soundResId = soundResId).apply {
            setVolume(1f, 1f)
            start()
        }
    }

    override fun keepAlive(soundResId: Int) {
        if (player?.isPlaying == false) {
            createPlayerIfNeeded(soundResId).apply {
                setVolume(0f, 0f)
                start()
            }
        }
    }

    private fun createPlayerIfNeeded(@RawRes soundResId: Int): MediaPlayer {
        return player
            ?.takeIf { lastSoundResId == soundResId }
            ?: createPlayer(soundResId)
    }

    private fun createPlayer(@RawRes soundResId: Int): MediaPlayer {
        val newPlayer = player?.apply { reset() } ?: MediaPlayer()
        return newPlayer
            .apply {
                audioSessionId = this@SoundPlayerImpl.audioSessionId
                setAudioAttributes(AudioAttributes.Builder().build())

                val adf = context.resources.openRawResourceFd(soundResId)
                setDataSource(adf)
                adf.close()

                isLooping = false
                setOnErrorListener(this@SoundPlayerImpl)
                prepare()
            }
            .also {
                lastSoundResId = soundResId
                player = it
            }
    }

    override fun close() {
        player?.release()
        player = null
        lastSoundResId = -1
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        TempoLogger.e(IllegalStateException("MediaPlayer error -> what: $what, extra: $extra"))
        return false
    }
}
