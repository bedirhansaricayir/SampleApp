package com.eterationcase.app.core.data.model

import com.eterationcase.app.core.database.entity.ProductEntity
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.core.network.model.response.NetworkProduct

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

fun Product.toEntity() = with(this) {
    ProductEntity(
        createdAt = createdAt,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        id = id
    )
}

fun NetworkProduct.toDomain() = with(this) {
    Product(
        createdAt = createdAt,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        id = id
    )
}