package com.enricog.tempo.navigation

import com.enricog.navigation.api.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun provideNavigator(impl: NavigatorImpl): Navigator
}
