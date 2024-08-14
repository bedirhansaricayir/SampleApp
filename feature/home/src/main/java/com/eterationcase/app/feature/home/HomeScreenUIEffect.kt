package com.eterationcase.app.feature.home

import com.eterationcase.app.core.base.viewmodel.Effect

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface HomeScreenUIEffect : Effect {
    data class NavigateToDetail(val productId: String) : HomeScreenUIEffect

}