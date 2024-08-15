package com.eterationcase.app.feature.home

import com.eterationcase.app.core.base.viewmodel.State
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface HomeScreenUIState : State {

    data object Loading : HomeScreenUIState
    data class Success(val data: List<Product>?, val brandList: List<String>?, val isFilterApplying: Boolean = false) : HomeScreenUIState
    data class Error(val message: String?) : HomeScreenUIState
}