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
}