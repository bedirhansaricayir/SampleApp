package com.eterationcase.app.feature.favorite

import com.eterationcase.app.core.base.viewmodel.Event

/**
 * Created by bedirhansaricayir on 16.08.2024
 */
sealed interface FavoriteScreenUIEvent : Event {
    data class OnFavoriteClicked(val productId: String) : FavoriteScreenUIEvent

}