package com.enricog.tempo.managers

import com.enricog.features.timer.WindowScreenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class ManagersModule {

    @Binds
    @Singleton
    abstract fun provideWindowScreenManager(impl: WindowScreenManagerImpl): WindowScreenManager
}
