package com.eterationcase.app.core.domain

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.domain.usecase.GetProductsUseCase
import com.eterationcase.app.core.model.Product
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by bedirhansaricayir on 16.08.2024
 */class GetProductsUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: Repository

    private lateinit var getProductsUseCase: GetProductsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getProductsUseCase = GetProductsUseCase(repository)
    }

    @Test
    fun `invoke should return expected products from repository`() = runBlocking {
        val expectedProducts = listOf(
            Product("1", "Product 1", "50.00", "Description 1", "Brand 1", "Model 1", "2023-07-17T07:21:02.529Z", "https://loremflickr.com/640/480/food"),
            Product("2", "Product 2", "75.00", "Description 2", "Brand 2", "Model 2", "2023-07-18T07:21:02.529Z", "https://loremflickr.com/640/480/food")
        )
        coEvery { repository.getProductsFromCache() } returns flow { emit(expectedProducts) }


        getProductsUseCase.invoke().collect { result ->

            assertThat(result).hasSize(2)
            assertThat(result).isEqualTo(expectedProducts)

        }

    }

    @Test
    fun `invoke should handle repository errors gracefully`() = runBlocking {
        val exception = Exception("Cache error")
        coEvery { repository.getProductsFromCache() } returns flow { throw exception }

        try {
            getProductsUseCase.invoke().toList()
            assert(false) { "Expected an exception to be thrown" }
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
    }
}