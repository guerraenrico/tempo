package com.enricog.core.coroutine.scope

import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@InstallIn(SingletonComponent::class)
@Module
internal object CoroutineScopeModule {

    @Singleton
    @ApplicationCoroutineScope
    @Provides
    fun providerCoroutineScope(dispatchers: CoroutineDispatchers): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatchers.cpu)
    }
}
