package com.eterationcase.app.feature.home

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<HomeScreenUIState, HomeScreenUIEvent, HomeScreenUIEffect>() {
    override fun setInitialState(): HomeScreenUIState = HomeScreenUIState.Loading

    override fun handleEvents(event: HomeScreenUIEvent) {
        when(event) {
            is HomeScreenUIEvent.OnProductClick -> {
                setEffect(HomeScreenUIEffect.NavigateToDetail(event.productId))
            }
            HomeScreenUIEvent.OnAddToCardClick -> {

            }
        }
    }

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect { response ->
                when(response) {
                    is Response.Error -> {
                        setState(HomeScreenUIState.Error(response.errorMessage))
                    }
                    Response.Loading -> {
                        setState(HomeScreenUIState.Loading)
                    }
                    is Response.Success -> {
                        response.data?.let { products ->
                            setState(HomeScreenUIState.Success(data = products))
                        }
                    }
                }
            }
        }
    }
}