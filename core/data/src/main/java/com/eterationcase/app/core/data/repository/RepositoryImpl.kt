package com.eterationcase.app.core.data.repository

import com.eterationcase.app.core.data.model.toDomain
import com.eterationcase.app.core.data.model.toEntity
import com.eterationcase.app.core.database.dao.ProductDao
import com.eterationcase.app.core.database.entity.toDomain
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.core.network.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
internal class RepositoryImpl @Inject constructor(
    private val localDataSource: ProductDao,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override suspend fun getProductsFromNetwork(): List<Product> {
       val productsNetwork = remoteDataSource.getProducts()
        return productsNetwork.map { it.toDomain() }
    }
    override suspend fun insertProductsToCache(products: List<Product>) {
        localDataSource.insertProducts(products.map { it.toEntity() })
    }
    override suspend fun getProductsFromCache(): List<Product> {
        val productsCache = localDataSource.getAllProducts()
        return productsCache.map { it.toDomain() }
    }
    override suspend fun getProductById(id: String): Flow<Product?> {
       val productEntity =  localDataSource.getProductById(id)
       return productEntity.map { it?.toDomain() }
    }
}