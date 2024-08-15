package com.eterationcase.app.feature.cart

import com.eterationcase.app.core.base.viewmodel.Event
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface CartScreenUIEvent : Event {
    data class OnIncreaseClick(val product: Product) : CartScreenUIEvent
    data class OnDecreaseClick(val product: Product) : CartScreenUIEvent
    data class CalculateTotalPrice(val products: List<Product>) : CartScreenUIEvent

}