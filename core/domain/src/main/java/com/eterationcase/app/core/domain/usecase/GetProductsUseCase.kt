package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

class GetProductsUseCase @Inject constructor(
    private val getProductsFromCacheUseCase: GetProductsFromCacheUseCase,
    private val getProductFromNetworkUseCase: GetProductFromNetworkUseCase,
    private val insertProductsToCacheUseCase: InsertProductsToCacheUseCase
) {
    operator fun invoke(): Flow<List<Product>> = flow {
        getProductsFromCacheUseCase.invoke().collect { productsFromCache ->
            if (productsFromCache.isNotEmpty()) {
                emit(productsFromCache)
            } else {
                val productsFromNetwork = getProductFromNetworkUseCase.invoke()

                insertProductsToCacheUseCase.invoke(productsFromNetwork)

                emit(productsFromNetwork)
            }
        }
    }.flowOn(Dispatchers.IO)
}