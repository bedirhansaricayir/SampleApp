package com.eterationcase.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eterationcase.app.core.database.dao.CartItemDao
import com.eterationcase.app.core.database.dao.ProductDao
import com.eterationcase.app.core.database.entity.CartItemEntity
import com.eterationcase.app.core.database.entity.ProductEntity

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Database(entities = [ProductEntity::class, CartItemEntity::class], version = 2)
internal abstract class Database: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
}