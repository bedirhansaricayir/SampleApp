package com.eterationcase.app.feature.detail

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.domain.usecase.AddToCartUseCase
import com.eterationcase.app.core.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    ): BaseViewModel<DetailScreenUIState,DetailScreenUIEvent,DetailScreenUIEffect>() {
    override fun setInitialState(): DetailScreenUIState = DetailScreenUIState.Loading

    override fun handleEvents(event: DetailScreenUIEvent) {
        when(event) {
            is DetailScreenUIEvent.GetProduct -> getProduct(event.productId)
            DetailScreenUIEvent.OnBackClicked -> setEffect(DetailScreenUIEffect.NavigateBack)
            is DetailScreenUIEvent.OnAddToCardClicked -> addToCart(event.productId)
            DetailScreenUIEvent.OnFavoriteClicked -> {}
        }
    }

    private fun getProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setState(DetailScreenUIState.Loading)
            getProductUseCase.invoke(productId).collect { product ->
                product?.let {
                    setState(DetailScreenUIState.Success(it))
                } ?: run {
                    setState(DetailScreenUIState.Error("Product not found"))
                }
            }
        }
    }
    private fun addToCart(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addToCartUseCase.invoke(productId)
        }
    }

}