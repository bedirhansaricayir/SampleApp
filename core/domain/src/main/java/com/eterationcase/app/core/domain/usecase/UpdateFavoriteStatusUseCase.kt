package com.eterationcase.app.core.domain.usecase

import com.eterationcase.app.core.data.repository.Repository
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 16.08.2024
 */
class UpdateFavoriteStatusUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(productId: String, isFavorite: Boolean) =
        repository.updateFavoriteStatus(productId, isFavorite)
}