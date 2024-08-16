package com.eterationcase.app.feature.favorite

import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.domain.usecase.GetFavoriteProductsUseCase
import com.eterationcase.app.core.domain.usecase.UpdateFavoriteStatusUseCase
import com.eterationcase.app.core.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 16.08.2024
 */

@HiltViewModel
class FavoriteScreenViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : BaseViewModel<FavoriteScreenUIState, FavoriteScreenUIEvent, FavoriteScreenUIEffect>() {

    val favoriteProducts: StateFlow<List<Product>> = getFavoriteProductsUseCase.invoke()
        .onStart { setState(FavoriteScreenUIState.Success(emptyList())) }
        .catch { exception ->
            setState(
                FavoriteScreenUIState.Error(
                    message = exception.message ?: "Something Went Wrong!"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    override fun setInitialState(): FavoriteScreenUIState = FavoriteScreenUIState.Loading

    override fun handleEvents(event: FavoriteScreenUIEvent) {
        when(event) {
            is FavoriteScreenUIEvent.OnFavoriteClicked ->  removeFromFavorites(event.productId)
        }
    }

    private fun removeFromFavorites(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateFavoriteStatusUseCase.invoke(productId = productId, isFavorite = false)
        }
    }
}