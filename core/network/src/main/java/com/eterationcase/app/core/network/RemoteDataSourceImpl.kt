package com.eterationcase.app.core.network

import com.eterationcase.app.core.common.response.Response
import com.eterationcase.app.core.network.api.RemoteApi
import com.eterationcase.app.core.network.model.response.NetworkProduct
import com.eterationcase.app.core.network.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

@Singleton
internal class RemoteDataSourceImpl @Inject constructor(
    private val api: RemoteApi
) : RemoteDataSource {
    override suspend fun getProducts(): Flow<Response<List<NetworkProduct>>> = flow {
        emit(Response.Loading)

        try {
            val response = api.getProducts()
            emit(Response.Success(response))
        } catch (e: Exception) {
            emit(Response.Error(errorMessage = "Something went wrong"))
        }
    }

}