package com.enricog.libraries.sound.api

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SoundModule {

    @Binds
    @Singleton
    abstract fun provideSoundPlayer(impl: SoundPlayerImpl): SoundPlayer
}
