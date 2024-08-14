package com.eterationcase.app.feature.detail

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
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
    private val getProductUseCase: GetProductUseCase
): BaseViewModel<DetailScreenUIState,DetailScreenUIEvent,DetailScreenUIEffect>() {
    override fun setInitialState(): DetailScreenUIState = DetailScreenUIState.Loading

    override fun handleEvents(event: DetailScreenUIEvent) {
        when(event) {
            is DetailScreenUIEvent.GetProduct -> getProduct(event.productId)
            DetailScreenUIEvent.OnBackClicked -> setEffect(DetailScreenUIEffect.NavigateBack)
            DetailScreenUIEvent.OnAddToCardClicked -> {}
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

}