package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.data.repository.Repository
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 16.08.2024
 */
class GetFavoriteProductsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke() = repository.getFavoriteProducts()
}