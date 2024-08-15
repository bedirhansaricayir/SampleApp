package com.eterationcase.app.feature.home

import com.eterationcase.app.core.base.viewmodel.Event

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface HomeScreenUIEvent : Event {
    data class OnProductClick(val productId: String) : HomeScreenUIEvent
    data class OnAddToCardClick(val productId: String) : HomeScreenUIEvent

}