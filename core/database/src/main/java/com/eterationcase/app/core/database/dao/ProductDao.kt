package com.eterationcase.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eterationcase.app.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE isFavorite = 1")
    fun getFavoriteProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET isFavorite = :isFavorite WHERE id = :productId")
    suspend fun updateFavoriteStatus(productId: String, isFavorite: Boolean)

    @Query("SELECT * FROM products")
    fun getProducts(): Flow<List<ProductEntity>>
}