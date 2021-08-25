package com.enricog.routines.di

import com.enricog.routines.navigation.RoutinesNavigationActions
import com.enricog.routines.navigation.RoutinesNavigationActionsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RoutinesModule {
    @Binds
    @ViewModelScoped
    abstract fun provideNavigationActions(impl: RoutinesNavigationActionsImpl): RoutinesNavigationActions
}