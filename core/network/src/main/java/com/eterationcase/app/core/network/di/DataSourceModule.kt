package com.eterationcase.app.core.network.di

import com.eterationcase.app.core.network.RemoteDataSourceImpl
import com.eterationcase.app.core.network.source.RemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {
    @Binds
    fun bindsDataSource(dataSource: RemoteDataSourceImpl): RemoteDataSource
}