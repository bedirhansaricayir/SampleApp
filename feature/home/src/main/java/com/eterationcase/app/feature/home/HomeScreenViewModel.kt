@file:OptIn(FlowPreview::class)

package com.eterationcase.app.feature.home

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.domain.usecase.AddToCartUseCase
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.feature.home.util.parseDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
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

    private val _selectedBrands: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val selectedBrands: StateFlow<List<String>> = _selectedBrands

    override fun setInitialState(): HomeScreenUIState = HomeScreenUIState.Loading

    override fun handleEvents(event: HomeScreenUIEvent) {
        when (event) {
            is HomeScreenUIEvent.OnProductClick -> {
                setEffect(HomeScreenUIEffect.NavigateToDetail(event.productId))
                if (_searchQuery.value.isNotEmpty()) onSearchQueryChanged("")
            }

            is HomeScreenUIEvent.OnAddToCardClick -> addToCart(event.productId)
            is HomeScreenUIEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
            is HomeScreenUIEvent.OnApplyFilter -> applyFilter(event.sortBy, event.brands)
            is HomeScreenUIEvent.OnBrandSelected -> onBrandSelected(event.brand)
            is HomeScreenUIEvent.OnClearFilter -> clearFilter()
        }
    }

    init {
        getProducts()
        viewModelScope.launch {
            _searchQuery.debounce(500).collect { query ->
                setState(
                    HomeScreenUIState.Success(data = filterListByBrands(query), brandList = (getCurrentState() as? HomeScreenUIState.Success)?.brandList )
                )
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect { response ->
                when (response) {
                    is Response.Error -> {
                        setState(HomeScreenUIState.Error(response.errorMessage))
                    }

                    Response.Loading -> {
                        setState(HomeScreenUIState.Loading)
                    }

                    is Response.Success -> {
                        response.data?.let { products ->
                            productList = products
                            setState(
                                HomeScreenUIState.Success(
                                    data = products,
                                    brandList = getBrandList()
                                )
                            )
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

    private fun filterListByBrands(query: String) =
        filterByBrands(filterList(query), _selectedBrands.value)

    private fun filterList(query: String) =
        productList?.filter { it.name.lowercase().contains(query.lowercase()) }

    private fun getBrandList() = productList?.map { it.brand }?.distinct()
    private fun applyFilter(
        sortBy: String,
        brands: List<String>?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val sortedList = when (sortBy) {
                "Old to new" -> sortByAscending(productList)
                "New to old" -> sortByDescending(productList)
                "Price high to low" -> priceHighToLow(productList)
                "Price low to high" -> priceLowToHigh(productList)
                else -> productList
            }

            val filteredList = filterByBrands(sortedList, brands)

            setSelectedBrands(brands)

            updateState { currentState ->
                (currentState as HomeScreenUIState.Success).copy(
                    data = filteredList,
                    isFilterApplying = true
                )
            }

        }
    }

    private fun sortByAscending(list: List<Product>?): List<Product>? {
        return list?.sortedBy { it.createdAt.parseDate() }
    }

    private fun sortByDescending(list: List<Product>?): List<Product>? {
        return list?.sortedByDescending { it.createdAt.parseDate() }
    }

    private fun priceHighToLow(list: List<Product>?): List<Product>? {
        return list?.sortedByDescending { it.price.toDouble() }
    }

    private fun priceLowToHigh(list: List<Product>?): List<Product>? {
        return list?.sortedBy { it.price.toDouble() }
    }

    private fun filterByBrands(sortedList: List<Product>?, brands: List<String>?): List<Product>? {
        return if (brands.isNullOrEmpty()) {
            sortedList
        } else {
            sortedList?.filter { it.brand in brands }
        }
    }

    private fun setSelectedBrands(brands: List<String>?) {
        if (brands?.isNotEmpty() == true) {
            _selectedBrands.value = brands
        }
    }

    private fun onBrandSelected(brand: String) {
        _selectedBrands.update { currentBrands ->
            if (currentBrands.contains(brand)) {
                currentBrands - brand
            } else {
                currentBrands + brand
            }
        }
    }

    private fun clearFilter() {
        updateState { currentState ->
            (currentState as HomeScreenUIState.Success).copy(
                data = productList,
                isFilterApplying = false
            )
        }
        clearSelectedBrands()
    }

    private fun clearSelectedBrands() {
        _selectedBrands.value = emptyList()
    }

}