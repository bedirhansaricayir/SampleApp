package com.eterationcase.app.core.domain

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.domain.usecase.GetProductUseCase
import com.eterationcase.app.core.model.Product
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by bedirhansaricayir on 16.08.2024
 */

class GetProductUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: Repository

    private lateinit var getProductUseCase: GetProductUseCase

    private val testProduct = Product(
        createdAt = "2023-07-17T07:21:02.529Z",
        name = "Bentley Focus",
        image = "https://loremflickr.com/640/480/food",
        price = "50.00",
        description = "description 1",
        model = "CTS",
        brand = "Lamborghini",
        id = "1"
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getProductUseCase = GetProductUseCase(repository)
    }

    @Test
    fun `invoke should return product`() = runBlocking {
        coEvery { repository.getProductById("1") } returns flow {
            emit(testProduct)
        }

        val resultFlow = getProductUseCase.invoke("1")
        val result = resultFlow.first()

        assertThat(result).isEqualTo(testProduct)
    }

    @Test
    fun `invoke should return null when product is not found`() = runBlocking {
        coEvery { repository.getProductById("2") } returns flow {
            emit(null)
        }

        val resultFlow = getProductUseCase.invoke("2")
        val result = resultFlow.first()

        assertThat(result).isNull()
    }
}