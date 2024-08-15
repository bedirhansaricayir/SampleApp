package com.eterationcase.app.core.database.di

import com.eterationcase.app.core.database.Database
import com.eterationcase.app.core.database.dao.CartItemDao
import com.eterationcase.app.core.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesProductDao(
        database: Database
    ): ProductDao = database.productDao()

    @Provides
    fun providesCartItemDao(
        database: Database
    ): CartItemDao = database.cartItemDao()
}