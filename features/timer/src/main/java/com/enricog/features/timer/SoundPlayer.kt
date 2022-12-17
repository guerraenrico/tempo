package com.enricog.features.timer

import androidx.annotation.RawRes
import java.io.Closeable

interface SoundPlayer : Closeable {

    fun play(@RawRes soundResId: Int)

    fun release(@RawRes soundResId: Int)
}