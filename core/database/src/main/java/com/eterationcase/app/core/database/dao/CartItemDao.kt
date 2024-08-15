package com.eterationcase.app.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eterationcase.app.core.database.entity.CartItemEntity
import com.eterationcase.app.core.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by bedirhansaricayir on 15.08.2024
 */

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItemEntity)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItemEntity>

    @Query("""
        SELECT products.*, cart_items.quantity 
        FROM products 
        INNER JOIN cart_items ON products.id = cart_items.productId
    """)
    fun getCartProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :productId")
    suspend fun increaseQuantity(productId: String)

    @Query("UPDATE cart_items SET quantity = quantity - 1 WHERE productId = :productId AND quantity > 1")
    suspend fun decreaseQuantity(productId: String)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItem(productId: String)

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartItemCount(): Flow<Int>
}