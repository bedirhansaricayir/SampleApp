package com.eterationcase.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eterationcase.app.core.domain.usecase.GetCartProductsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 15.08.2024
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCartProductsCountUseCase: GetCartProductsCountUseCase
) : ViewModel() {

    val cartItemCount: StateFlow<Int> = getCartProductsCountUseCase.invoke()
        .stateIn(
        scope = viewModelScope,
        initialValue = 0,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}