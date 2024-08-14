package com.eterationcase.app.core.database.di

import android.content.Context
import androidx.room.Room
import com.eterationcase.app.core.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesLiftingDatabase(
        @ApplicationContext context: Context,
    ): Database = Room.databaseBuilder(
        context,
        Database::class.java,
        "product_database",
    ).fallbackToDestructiveMigration()
        .build()
}