package com.eterationcase.app.feature.home

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.domain.usecase.AddToCartUseCase
import com.eterationcase.app.core.domain.usecase.GetProductFromNetworkUseCase
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import com.eterationcase.app.core.domain.usecase.InsertProductsToCacheUseCase
import com.eterationcase.app.core.domain.usecase.UpdateFavoriteStatusUseCase
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.feature.home.util.parseDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val getProductFromNetworkUseCase: GetProductFromNetworkUseCase,
    private val insertProductsToCacheUseCase: InsertProductsToCacheUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase,
) : BaseViewModel<HomeScreenUIState, HomeScreenUIEvent, HomeScreenUIEffect>() {

    private var productList: List<Product>? = null

    private val _selectedBrands: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val selectedBrands: StateFlow<List<String>> = _selectedBrands

    private val selectedSortBy = MutableStateFlow("")

    private val _products = MutableStateFlow<List<Product>>(emptyList())

    private val _searchQuery = MutableStateFlow("")

    val products: StateFlow<List<Product>> = combine(
        _products,
        selectedBrands,
        _searchQuery,
        selectedSortBy
    ) { products, selectedBrands, searchQuery, sortBy ->

        productList = products
        var filteredProducts = products

        if (selectedBrands.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { it.brand in selectedBrands }
        }

        if (searchQuery.isNotEmpty()) {
            filteredProducts =
                filteredProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }
        }

        if (sortBy.isNotEmpty()) {
            filteredProducts = when (sortBy) {
                "Old to new" -> sortByAscending(filteredProducts)
                "New to old" -> sortByDescending(filteredProducts)
                "Price high to low" -> priceHighToLow(filteredProducts)
                "Price low to high" -> priceLowToHigh(filteredProducts)
                else -> filteredProducts
            }
        }

        updateState { currentState ->
            (currentState as HomeScreenUIState.Success).copy(brandList = getBrandList())
        }


        filteredProducts

    }.onStart { setState(HomeScreenUIState.Success(emptyList(), brandList = emptyList())) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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
            is HomeScreenUIEvent.OnFavoriteClick -> updateFavoriteStatus(event.productId, event.isFavorite)
        }
    }

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase.invoke().collect {
              if (it.isEmpty()) {
                  val fromNetwork = getProductFromNetworkUseCase.invoke()
                  insertProductsToCacheUseCase.invoke(fromNetwork)
              }
              productList = it
              _products.value = it
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
            selectedSortBy.value = sortBy
            val filteredList = filterByBrands(productList, brands)

            setSelectedBrands(brands)

            updateState { currentState ->
                (currentState as HomeScreenUIState.Success).copy(
                    data = filteredList,
                    isFilterApplying = true
                )
            }

        }
    }

    private fun sortByAscending(list: List<Product>): List<Product> {
        return list.sortedBy { it.createdAt.parseDate() }
    }

    private fun sortByDescending(list: List<Product>): List<Product> {
        return list.sortedByDescending { it.createdAt.parseDate() }
    }

    private fun priceHighToLow(list: List<Product>): List<Product> {
        return list.sortedByDescending { it.price.toDouble() }
    }

    private fun priceLowToHigh(list: List<Product>): List<Product> {
        return list.sortedBy { it.price.toDouble() }
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

    private fun updateFavoriteStatus(productId: String, isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.invoke(productId = productId, isFavorite = isFavorite)
        }
    }

}