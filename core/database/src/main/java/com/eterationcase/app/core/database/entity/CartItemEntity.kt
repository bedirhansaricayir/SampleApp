package com.eterationcase.app.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Created by bedirhansaricayir on 15.08.2024
 */

@Entity(
    tableName = "cart_items",
    foreignKeys = [ForeignKey(
        entity = ProductEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("productId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val cartItemId: Long = 0,
    val productId: String,
    val quantity: Int = 1

)