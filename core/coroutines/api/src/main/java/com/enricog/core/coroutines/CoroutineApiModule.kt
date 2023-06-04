package com.enricog.core.coroutines

import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CoroutineApiModule {

    @Binds
    abstract fun provideCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}
