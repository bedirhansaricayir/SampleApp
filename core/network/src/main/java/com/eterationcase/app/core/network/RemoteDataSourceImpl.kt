package com.eterationcase.app.core.network

import com.eterationcase.app.core.network.api.RemoteApi
import com.eterationcase.app.core.network.model.response.NetworkProduct
import com.eterationcase.app.core.network.source.RemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

@Singleton
internal class RemoteDataSourceImpl @Inject constructor(
    private val api: RemoteApi
) : RemoteDataSource {
    override suspend fun getProducts(): List<NetworkProduct> = api.getProducts()

}