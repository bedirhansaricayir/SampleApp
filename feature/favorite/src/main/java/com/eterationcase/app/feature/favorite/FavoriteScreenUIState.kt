package com.eterationcase.app.feature.favorite

import com.eterationcase.app.core.base.viewmodel.State
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 16.08.2024
 */
sealed interface FavoriteScreenUIState : State {
    data object Loading : FavoriteScreenUIState
    data class Success(val data: List<Product>?) : FavoriteScreenUIState
    data class Error(val message: String) : FavoriteScreenUIState

}