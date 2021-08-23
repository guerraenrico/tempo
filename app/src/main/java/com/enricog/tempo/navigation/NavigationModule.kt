package com.enricog.tempo.navigation

import com.enricog.navigation.Navigator
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
        return NavigatorImpl()
    }
}
