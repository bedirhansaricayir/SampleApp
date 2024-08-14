package com.eterationcase.app.feature.home

import com.eterationcase.app.core.base.viewmodel.BaseViewModel
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase
) : BaseViewModel<HomeScreenUIState, HomeScreenUIEvent, HomeScreenUIEffect>() {
    override fun setInitialState(): HomeScreenUIState = HomeScreenUIState.Loading

    override fun handleEvents(event: HomeScreenUIEvent) {

    }

}