package com.eterationcase.app.feature.cart

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.domain.usecase.DecreaseQuantityUseCase
import com.eterationcase.app.core.domain.usecase.GetCartProductsUseCase
import com.eterationcase.app.core.domain.usecase.IncreaseQuantityUseCase
import com.eterationcase.app.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
@HiltViewModel
class CartScreenViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val decreaseQuantityUseCase: DecreaseQuantityUseCase,
    private val increaseQuantityUseCase: IncreaseQuantityUseCase,
) : BaseViewModel<CartScreenUIState, CartScreenUIEvent, CartScreenUIEffect>() {

    private val _totalPrice = MutableStateFlow(0)
    val totalPrice: StateFlow<Int> = _totalPrice

    val cartProducts: StateFlow<List<Product>> = getCartProductsUseCase.invoke()
        .onStart { setState(CartScreenUIState.Success(data = emptyList())) }
        .catch { exception ->
            setState(
                CartScreenUIState.Error(
                    message = exception.message ?: "Something Went Wrong!"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun setInitialState(): CartScreenUIState = CartScreenUIState.Loading

    override fun handleEvents(event: CartScreenUIEvent) {
        when (event) {
            is CartScreenUIEvent.OnDecreaseClick -> decreaseQuantity(event.product.id)
            is CartScreenUIEvent.OnIncreaseClick -> increaseQuantity(event.product.id)
            is CartScreenUIEvent.CalculateTotalPrice -> calculateTotalPrice(event.products)
        }
    }

    private fun decreaseQuantity(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            decreaseQuantityUseCase.invoke(productId = productId)
        }
    }

    private fun increaseQuantity(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            increaseQuantityUseCase.invoke(productId = productId)
        }
    }

    private fun calculateTotalPrice(products: List<Product>) {
        val total = products.sumOf { product -> product.price.toDouble().toInt() * product.quantity!! }
        _totalPrice.value = total
    }
}