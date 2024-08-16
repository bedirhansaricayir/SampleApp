package com.eterationcase.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Entity(tableName = "products")
data class ProductEntity(
    val createdAt: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val quantity: Int? = null,
    val isFavorite: Boolean = false
)

fun ProductEntity.toDomain() = with(this) {
    Product(
        createdAt = createdAt,
        name = name,
        image = image,
        price = price,
        description = description,
        model = model,
        brand = brand,
        id = id,
        quantity = quantity,
        isFavorite = isFavorite
    )
}