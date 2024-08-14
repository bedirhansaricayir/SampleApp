package com.eterationcase.app.core.network.source

import com.eterationcase.app.core.network.model.response.NetworkProduct

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

interface RemoteDataSource {
    suspend fun getProducts(): List<NetworkProduct>
}