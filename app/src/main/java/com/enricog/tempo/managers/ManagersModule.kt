package com.enricog.tempo.managers

import com.enricog.timer.WindowScreenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
internal abstract class ManagersModule {

    @Binds
    abstract fun provideWindowScreenManager(impl: WindowScreenManagerImpl): WindowScreenManager

}