package com.enricog.core.date.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

@Module
@InstallIn(SingletonComponent::class)
class DateApiModule {

    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()
}