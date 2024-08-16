package com.eterationcase.app.core.domain

import com.eterationcase.app.core.data.repository.Repository
import com.eterationcase.app.core.domain.usecase.AddToCartUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by bedirhansaricayir on 16.08.2024
 */

class AddToCartUseCaseTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    lateinit var repository: Repository

    private lateinit var addToCartUseCase: AddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        addToCartUseCase = AddToCartUseCase(repository)
    }

    @Test
    fun `invoke should call addToCart on repository`() = runBlocking {
        val productId = "1"
        coEvery { repository.addToCart(productId) } returns Unit

        addToCartUseCase.invoke(productId)

        coVerify { repository.addToCart(productId) }
    }

    @Test
    fun `invoke should handle repository errors gracefully`() = runBlocking {
        val productId = "1"
        val exception = Exception("Network error")
        coEvery { repository.addToCart(productId) } throws exception

        try {
            addToCartUseCase.invoke(productId)
            assert(false) { "Expected an exception to be thrown" }
        } catch (e: Exception) {
            assertThat(e).isEqualTo(exception)
        }
    }
}