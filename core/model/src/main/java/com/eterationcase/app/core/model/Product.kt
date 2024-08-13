package com.eterationcase.app.core.model

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

data class Product(
    val createdAt: String,
    val name: String,
    val image: String,
    val price: String,
    val description: String,
    val model: String,
    val brand: String,
    val id: String
)
