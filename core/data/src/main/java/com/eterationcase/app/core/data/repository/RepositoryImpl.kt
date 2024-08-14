package com.eterationcase.app.core.data.repository

import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.data.model.toDomain
import com.eterationcase.app.core.data.model.toEntity
import com.eterationcase.app.core.database.dao.ProductDao
import com.eterationcase.app.core.database.entity.toDomain
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.core.network.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
internal class RepositoryImpl @Inject constructor(
    private val localDataSource: ProductDao,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override suspend fun getProductsFromNetwork(): Flow<Response<List<Product>>> = flow {
        remoteDataSource.getProducts().collect { response ->
            when (response) {
                is Response.Success -> {
                    response.data?.let { networkProductList ->
                        emit(Response.Success(networkProductList.map { it.toDomain() }))
                    }
                }

                is Response.Error -> {
                    emit(Response.Error(errorMessage = response.errorMessage))
                }

                Response.Loading -> {
                    emit(Response.Loading)
                }
            }
        }
    }

    override suspend fun insertProductsToCache(products: List<Product>) {
        localDataSource.insertProducts(products.map { it.toEntity() })
    }

    override suspend fun getProductsFromCache(): Flow<Response<List<Product>>> = flow {
        localDataSource.getAllProducts().collect { cachedProductList ->
            emit(Response.Loading)

            try {
                emit(Response.Success(cachedProductList.map { it.toDomain() }))
            } catch (e: Exception) {
                emit(Response.Error(errorMessage = e.message ?: "Something Went Wrong!"))
            }
        }

    }

    override suspend fun getProductById(id: String): Flow<Product?> {
       val productEntity =  localDataSource.getProductById(id)
       return productEntity.map { it?.toDomain() }
    }
}