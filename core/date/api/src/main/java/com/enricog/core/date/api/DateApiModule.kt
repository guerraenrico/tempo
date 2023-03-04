package com.enricog.core.date.api

import android.annotation.SuppressLint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock

@Module
@InstallIn(SingletonComponent::class)
class DateApiModule {

    @SuppressLint("NewApi")
    @Provides
    fun provideClock(): Clock = Clock.systemDefaultZone()
}