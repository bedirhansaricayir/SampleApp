package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

class GetProductsUseCase @Inject constructor(
    private val repository: Repository,
    private val insertProductsToCacheUseCase: InsertProductsToCacheUseCase
) {
    suspend operator fun invoke(): Flow<Response<List<Product>>> = flow {
        try {
            emit(Response.Loading)

            val productsFromCache = repository.getProductsFromCache()
            if (productsFromCache.isNotEmpty()) {
                emit(Response.Success(data = productsFromCache))
            } else {
                val productsFromApi = repository.getProductsFromNetwork()
                insertProductsToCacheUseCase.invoke(productsFromApi)
                emit(Response.Success(data = productsFromApi))
            }

        } catch (e: IOException) {
            emit(Response.Error(errorMessage = "Please check your internet connection."))
        } catch (e: Exception) {
            emit(Response.Error(errorMessage = e.message ?: "Something Went Wrong!"))
        }
    }
}