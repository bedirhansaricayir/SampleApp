package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.model.Product
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
class InsertProductsToCacheUseCase @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(products: List<Product>) =
        repository.insertProductsToCache(products)

}