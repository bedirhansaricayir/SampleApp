package com.eterationcase.app.core.data.repository

import com.eterationcase.app.core.data.model.toDomain
import com.eterationcase.app.core.data.model.toEntity
import com.eterationcase.app.core.database.dao.CartItemDao
import com.eterationcase.app.core.database.dao.ProductDao
import com.eterationcase.app.core.database.entity.CartItemEntity
import com.eterationcase.app.core.database.entity.toDomain
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.core.network.source.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by bedirhansaricayir on 14.08.2024
 */
internal class RepositoryImpl @Inject constructor(
    private val localDataSource: ProductDao,
    private val cartItemDao: CartItemDao,
    private val remoteDataSource: RemoteDataSource
) : Repository {
    override suspend fun getProductsFromNetwork(): List<Product> {
       val productsNetwork = remoteDataSource.getProducts()
        return productsNetwork.map { it.toDomain() }
    }
    override suspend fun insertProductsToCache(products: List<Product>) {
        localDataSource.insertProducts(products.map { it.toEntity() })
    }
    override suspend fun getProductsFromCache(): List<Product> {
        val productsCache = localDataSource.getAllProducts()
        return productsCache.map { it.toDomain() }
    }
    override suspend fun getProductById(id: String): Flow<Product?> {
       val productEntity =  localDataSource.getProductById(id)
       return productEntity.map { it?.toDomain() }
    }
    override suspend fun addToCart(productId: String) {
        val cartItemEntity = cartItemDao.getAllCartItems().find { it.productId == productId }
        if (cartItemEntity != null) {
            cartItemDao.increaseQuantity(productId)
        } else {
            cartItemDao.insertCartItem(CartItemEntity(productId = productId))
        }
    }
    override fun getCardProducts(): Flow<List<Product>> {
       return cartItemDao.getCartProducts().map { list -> list.map { it.toDomain() } }
    }
    override suspend fun increaseQuantity(productId: String) {
        return cartItemDao.increaseQuantity(productId)
    }
    override suspend fun decreaseQuantity(productId: String) {
        val cartItemEntity = cartItemDao.getAllCartItems().find { it.productId == productId }
        if (cartItemEntity != null) {
            if (cartItemEntity.quantity > 1) {
                cartItemDao.decreaseQuantity(productId)
            } else {
                deleteProductFromCart(productId)
            }
        }
    }
    override suspend fun deleteProductFromCart(productId: String) {
        return cartItemDao.deleteCartItem(productId)
    }

    override fun getCartProductsCount(): Flow<Int> {
        return cartItemDao.getCartItemCount()
    }

}