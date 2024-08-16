package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

class GetProductsUseCase @Inject constructor(
    private val repository: Repository,
) {
    operator fun invoke(): Flow<List<Product>> = repository.getProductsFromCache()
}