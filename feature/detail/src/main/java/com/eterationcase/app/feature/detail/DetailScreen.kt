package com.eterationcase.app.feature.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel(),
    productId: String,
    onNavigateBack: () -> Unit
) {
    val detailUIState: DetailScreenUIState by viewModel.state.collectAsStateWithLifecycle()
    val detailScreenUIEffect = viewModel.effect

    LaunchedEffect(Unit) {
        viewModel.setEvent(DetailScreenUIEvent.GetProduct(productId))
    }

    LaunchedEffect(detailScreenUIEffect) {
        detailScreenUIEffect.collect { effect ->
            when (effect) {
                DetailScreenUIEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    DetailScreenContent(
        modifier = modifier,
        state = detailUIState,
        onEvent = viewModel::setEvent
    )

}

@Composable
fun DetailScreenContent(
    modifier: Modifier = Modifier,
    state: DetailScreenUIState,
    onEvent: (DetailScreenUIEvent) -> Unit
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(
                animationSpec = tween(1000)
            ) togetherWith fadeOut(animationSpec = tween(1000))
        },
        label = ""
    ) { state ->
        when (state) {
            DetailScreenUIState.Loading -> LoadingScreen()
            is DetailScreenUIState.Error -> ErrorScreen(errorMessage = state.message)
            is DetailScreenUIState.Success -> DetailScreenProductContent(
                state = state,
                onEvent = onEvent
            )
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
fun DetailScreenProductContent(
    modifier: Modifier = Modifier,
    state: DetailScreenUIState.Success,
    onEvent: (DetailScreenUIEvent) -> Unit
) {
    Scaffold(
        topBar = {
            DetailScreenTopBar(
                title = state.data.name,
                onBackClick = { onEvent(DetailScreenUIEvent.OnBackClicked) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth,
                        model = state.data.image,
                        contentDescription = "Product Image"
                    )
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd),
                        onClick = { onEvent(DetailScreenUIEvent.OnFavoriteClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "Favorite Button",
                            tint = Color.White
                        )
                    }
                }

                Text(
                    text = state.data.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = state.data.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }

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
                    Text(text = "Price:", color = Color(0xFF2A59FE))
                    Text(
                        text = "${state.data.price} ₺",
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
                    onClick = { onEvent(DetailScreenUIEvent.OnAddToCardClicked(state.data.id))}
                ) {
                    Text(text = "Add to Cart")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit
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
        navigationIcon = {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Button"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF2A59FE),
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}
