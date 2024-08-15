package com.eterationcase.app.feature.cart

import com.eterationcase.app.core.base.viewmodel.State
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface CartScreenUIState : State {
    data object Loading : CartScreenUIState
    data class Success(val data: List<Product>) : CartScreenUIState
    data class Error(val message: String) : CartScreenUIState
}