package com.eterationcase.app.core.network.model.response

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

data class NetworkProduct(
    val createdAt: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val id: String
)

