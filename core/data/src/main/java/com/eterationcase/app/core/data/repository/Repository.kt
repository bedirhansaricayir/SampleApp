package com.eterationcase.app.core.data.repository

import com.eterationcase.app.core.model.Product
import kotlinx.coroutines.flow.Flow

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
interface Repository {

    suspend fun getProductsFromNetwork(): List<Product>

    suspend fun insertProductsToCache(products: List<Product>)

    suspend fun getProductsFromCache(): List<Product>

    suspend fun getProductById(id: String): Flow<Product?>

    suspend fun addToCart(productId: String)

    fun getCardProducts(): Flow<List<Product>>

    suspend fun increaseQuantity(productId: String)

    suspend fun decreaseQuantity(productId: String)

    suspend fun deleteProductFromCart(productId: String)

    fun getCartProductsCount(): Flow<Int>
}