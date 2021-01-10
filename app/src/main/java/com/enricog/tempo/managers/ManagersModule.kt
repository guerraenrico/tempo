package com.enricog.tempo.managers

import com.enricog.timer.WindowScreenManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
internal abstract class ManagersModule {

    @Binds
    abstract fun provideWindowScreenManager(impl: WindowScreenManagerImpl): WindowScreenManager

}