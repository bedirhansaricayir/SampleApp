package com.eterationcase.app.core.data.di

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.data.repository.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    internal abstract fun bindsRepository(repositoryImpl: RepositoryImpl): Repository
}