package com.enricog.localdatasource

import android.content.Context
import com.enricog.data.datasource.RoutineDataSource
import com.enricog.localdatasource.routine.RoutineDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object LocalDataSourceModule {

    @Provides
    @Singleton
    internal fun provideDatabase(@ApplicationContext context: Context): TempoDatabase {
        return TempoDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    internal fun provideRoutineDataSource(impl: RoutineDataSourceImpl): RoutineDataSource {
        return impl
    }
}
