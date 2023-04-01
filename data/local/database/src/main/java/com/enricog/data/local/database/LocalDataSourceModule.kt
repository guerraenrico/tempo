package com.enricog.data.local.database

import android.content.Context
import com.enricog.data.local.database.routines.RoutineDataSourceImpl
import com.enricog.data.local.database.timer.theme.TimerThemeDataSourceImpl
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.timer.api.theme.TimerThemeDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourceModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): TempoDatabase {
        return TempoDatabase.getInstance(context = context, json = Json)
    }

    @Provides
    @Singleton
    internal fun provideRoutineDataSource(impl: RoutineDataSourceImpl): RoutineDataSource {
        return impl
    }

    @Provides
    @Singleton
    internal fun provideTimerThemeDataSource(impl: TimerThemeDataSourceImpl): TimerThemeDataSource {
        return impl
    }
}
