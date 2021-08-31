package com.enricog.core.coroutine.dispatchers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class CoroutineDispatchersModule {
    @Singleton
    @Binds
    abstract fun provideCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}