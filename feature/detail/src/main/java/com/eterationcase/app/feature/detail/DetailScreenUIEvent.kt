package com.eterationcase.app.feature.detail

import com.eterationcase.app.core.base.viewmodel.Event

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

sealed interface DetailScreenUIEvent : Event {
    data class GetProduct(val productId: String) : DetailScreenUIEvent
    data object OnBackClicked : DetailScreenUIEvent
    data class OnAddToCardClicked(val productId: String) : DetailScreenUIEvent
    data class OnFavoriteClicked(val productId: String, val isFavorite: Boolean) : DetailScreenUIEvent
}