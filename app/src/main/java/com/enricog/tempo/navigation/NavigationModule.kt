package com.enricog.tempo.navigation

import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.timer.navigation.TimerNavigationActions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object NavigationModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator {
        return Navigator()
    }

    @Provides
    fun provideTimerNavigationActions(impl: Navigator): TimerNavigationActions {
        return impl
    }

    @Provides
    fun provideRoutinesNavigationActions(impl: Navigator): RoutinesNavigationActions {
        return impl
    }
}
