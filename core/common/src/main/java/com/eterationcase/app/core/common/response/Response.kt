package com.eterationcase.app.core.common.response

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

sealed interface Response<out T> {
    class Success<T>(val data: T?) : Response<T>
    class Error<T>(val errorMessage: String) : Response<T>
    data object Loading : Response<Nothing>
}