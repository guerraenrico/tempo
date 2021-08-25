package com.enricog.timer.di

import com.enricog.timer.navigation.TimerNavigationActions
import com.enricog.timer.navigation.TimerNavigationActionsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class TimerModule {
    @Binds
    @ViewModelScoped
    abstract fun provideNavigationActions(impl: TimerNavigationActionsImpl): TimerNavigationActions
}