package com.eterationcase.app.feature.cart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: CartScreenViewModel = hiltViewModel(),
) {
    val cartUIState: CartScreenUIState by viewModel.state.collectAsStateWithLifecycle()
    val cartScreenUIEffect = viewModel.effect
    val products by viewModel.cartProducts.collectAsStateWithLifecycle()
    val totalPrice by viewModel.totalPrice.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        cartScreenUIEffect.collect { effect ->

        }
    }

    CartScreenContent(
        modifier = modifier,
        state = cartUIState,
        onEvent = viewModel::setEvent,
        products = products,
        totalPrice = totalPrice
    )
}

@Composable
fun CartScreenContent(
    modifier: Modifier = Modifier,
    state: CartScreenUIState,
    onEvent: (CartScreenUIEvent) -> Unit,
    products: List<Product>,
    totalPrice: Int
) {
    AnimatedContent(
        modifier = modifier,
        targetState = state,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(1000)
            ) togetherWith fadeOut(animationSpec = tween(1000))
        },
        label = ""
    ) { state ->
        when (state) {
            is CartScreenUIState.Error -> ErrorScreen(errorMessage = state.message)
            CartScreenUIState.Loading -> LoadingScreen()
            is CartScreenUIState.Success -> CartScreenListContent(state = products, totalPrice = totalPrice, onEvent = onEvent)
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Loading...")
    }
}

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = errorMessage)
    }
}

@Composable
fun CartScreenListContent(
    modifier: Modifier = Modifier,
    state: List<Product>,
    totalPrice: Int,
    onEvent: (CartScreenUIEvent) -> Unit
) {
    LaunchedEffect(state) {
        onEvent(CartScreenUIEvent.CalculateTotalPrice(state))
    }

    Scaffold(
        topBar = {
            CartScreenTopBar(title = "E-Market")
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state) { product ->
                    CartListItem(
                        product = product,
                        onIncreaseClick = { onEvent(CartScreenUIEvent.OnIncreaseClick(it)) },
                        onDecreaseClick = { onEvent(CartScreenUIEvent.OnDecreaseClick(it)) }
                    )
                }
            }
            if (totalPrice > 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total:", color = Color(0xFF2A59FE))
                        Text(
                            text = "$totalPrice ₺",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    ElevatedButton(
                        modifier = Modifier
                            .padding(8.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Color(0xFF2A59FE),
                            contentColor = Color.White
                        ),
                        onClick = { }
                    ) {
                        Text(text = "Complete")
                    }
                }
            }

        }
    }
}

@Composable
fun CartListItem(
    modifier: Modifier = Modifier,
    product: Product,
    onIncreaseClick: (Product) -> Unit,
    onDecreaseClick: (Product) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = product.name)
            Text(
                text = "${product.price}₺",
                fontSize = 12.sp,
                color = Color(0xFF2A59FE)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CounterSurface(
                text = "-",
                onClick = { onDecreaseClick(product) }
            )
            CounterSurface(
                containerColor = Color(0xFF2A59FE),
                contentColor = Color.White,
                textColor = Color.White,
                text = (product.quantity ?: "1").toString(),
                enabled = false,
                onClick = {}
            )
            CounterSurface(
                text = "+",
                onClick = { onIncreaseClick(product) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenTopBar(
    modifier: Modifier = Modifier,
    title: String,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2A59FE),
            titleContentColor = Color.White
        )
    )
}

@Composable
fun CounterSurface(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.LightGray.copy(alpha = 0.3f),
    contentColor: Color = Color.Black,
    text: String,
    textColor: Color = Color.Black,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .size(40.dp)
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(0.dp),
        color = containerColor,
        contentColor = contentColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = textColor)
        }
    }
}