package com.eterationcase.app.core.network.source

import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.network.model.response.NetworkProduct
import kotlinx.coroutines.flow.Flow

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

interface RemoteDataSource {
    suspend fun getProducts(): Flow<Response<List<NetworkProduct>>>
}