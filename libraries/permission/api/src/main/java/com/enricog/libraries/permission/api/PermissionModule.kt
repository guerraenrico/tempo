package com.enricog.libraries.permission.api

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class PermissionModule {

    @Binds
    abstract fun providePermissionManager(impl: PermissionManagerImpl): PermissionManager
}