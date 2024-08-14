package com.eterationcase.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eterationcase.app.core.database.dao.ProductDao
import com.eterationcase.app.core.database.entity.ProductEntity

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Database(entities = [ProductEntity::class], version = 1)
internal abstract class Database: RoomDatabase() {
    abstract fun productDao(): ProductDao
}