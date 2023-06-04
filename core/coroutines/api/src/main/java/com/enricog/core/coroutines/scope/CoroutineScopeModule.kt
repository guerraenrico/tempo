package com.enricog.core.coroutines.scope

import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineScopeModule {

    @Provides
    @Singleton
    @ApplicationCoroutineScope
    fun providerCoroutineScope(dispatchers: CoroutineDispatchers): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatchers.cpu)
    }
}