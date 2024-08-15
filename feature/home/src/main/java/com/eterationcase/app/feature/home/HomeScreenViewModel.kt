@file:OptIn(FlowPreview::class)

package com.eterationcase.app.feature.home

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.domain.usecase.AddToCartUseCase
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import com.eterationcase.app.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
) : BaseViewModel<HomeScreenUIState, HomeScreenUIEvent, HomeScreenUIEffect>() {

    private var productList: List<Product>? = null
    private val _searchQuery = MutableStateFlow("")

    override fun setInitialState(): HomeScreenUIState = HomeScreenUIState.Loading

    override fun handleEvents(event: HomeScreenUIEvent) {
        when(event) {
            is HomeScreenUIEvent.OnProductClick -> {
                setEffect(HomeScreenUIEffect.NavigateToDetail(event.productId))
                if (_searchQuery.value.isNotEmpty()) onSearchQueryChanged("")
            }
            is HomeScreenUIEvent.OnAddToCardClick -> addToCart(event.productId)
            is HomeScreenUIEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
        }
    }

    init {
        getProducts()
        viewModelScope.launch {
            _searchQuery.debounce(500).collect { query ->
                updateState { currentState ->
                    (currentState as HomeScreenUIState.Success).copy(data = filterList(query))
                }
            }
        }
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
                            productList = products
                            setState(HomeScreenUIState.Success(data = products))
                        }
                    }
                }
            }
        }
    }

    private fun addToCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.invoke(productId)
        }
    }

    private fun onSearchQueryChanged(it: String) {
        _searchQuery.value = it
    }

    private fun filterList(query: String) = productList?.filter { it.name.lowercase().contains(query.lowercase()) }

}