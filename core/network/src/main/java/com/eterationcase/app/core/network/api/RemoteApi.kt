package com.eterationcase.app.core.network.api

import com.eterationcase.app.core.network.model.response.NetworkProduct
import retrofit2.http.GET

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

internal interface RemoteApi {

    companion object {
        const val BASE_URL = "https://5fc9346b2af77700165ae514.mockapi.io/"
    }

    @GET(value = "products")
    suspend fun getProducts(): List<NetworkProduct>
}