package com.enricog.core.inject

import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchersImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
internal abstract class CoreModule {

    @Binds
    abstract fun provideCoroutineDispatchers(impl: CoroutineDispatchersImpl): CoroutineDispatchers
}