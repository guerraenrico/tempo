package com.enricog.features.timer

import com.enricog.features.timer.service.TimerServiceHandler
import com.enricog.features.timer.service.TimerServiceHandlerImpl
import com.enricog.features.timer.util.TimerProvider
import com.enricog.features.timer.util.TimerProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TimerModule {

    @Binds
    abstract fun provideTimerServiceHandler(impl: TimerServiceHandlerImpl): TimerServiceHandler

    @Binds
    abstract fun provideTimerProvider(impl: TimerProviderImpl): TimerProvider
}