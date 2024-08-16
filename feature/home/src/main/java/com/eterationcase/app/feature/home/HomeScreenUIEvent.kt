package com.eterationcase.app.feature.home

import com.eterationcase.app.core.base.viewmodel.Event

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface HomeScreenUIEvent : Event {
    data class OnProductClick(val productId: String) : HomeScreenUIEvent
    data class OnAddToCardClick(val productId: String) : HomeScreenUIEvent
    data class OnSearchQueryChanged(val searchQuery: String) : HomeScreenUIEvent
    data class OnApplyFilter(val sortBy: String, val brands: List<String>?) : HomeScreenUIEvent
    data class OnBrandSelected(val brand: String) : HomeScreenUIEvent
    data object OnClearFilter : HomeScreenUIEvent
    data class OnFavoriteClick(val productId: String, val isFavorite: Boolean) : HomeScreenUIEvent

}