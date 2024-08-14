package com.eterationcase.app.feature.detail

import com.eterationcase.app.core.base.viewmodel.State
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

sealed interface DetailScreenUIState : State {
    data object Loading: DetailScreenUIState
    data class Success(val data: Product): DetailScreenUIState
    data class Error(val message: String): DetailScreenUIState

}