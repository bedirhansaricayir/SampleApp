package com.eterationcase.app.feature.detail

import com.eterationcase.app.core.base.viewmodel.Effect

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
sealed interface DetailScreenUIEffect : Effect {
    data object NavigateBack : DetailScreenUIEffect
}