package com.eterationcase.app.feature.favorite

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.eterationcase.app.core.model.Product

/**
 * Created by bedirhansaricayir on 16.08.2024
 */

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteScreenViewModel = hiltViewModel()
) {
    val favoriteUIState: FavoriteScreenUIState by viewModel.state.collectAsStateWithLifecycle()
    val favorites by viewModel.favoriteProducts.collectAsStateWithLifecycle()

    FavoriteScreenContent(
        state = favoriteUIState,
        favorites = favorites,
        onEvent = viewModel::setEvent
    )

}

@Composable
fun FavoriteScreenContent(
    modifier: Modifier = Modifier,
    state: FavoriteScreenUIState,
    favorites: List<Product>?,
    onEvent: (FavoriteScreenUIEvent) -> Unit
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
            FavoriteScreenUIState.Loading -> LoadingScreen()
            is FavoriteScreenUIState.Error -> ErrorScreen(errorMessage = state.message)
            is FavoriteScreenUIState.Success -> FavoriteListScreen(state = favorites, onEvent = onEvent)
        }
    }
}

@Composable
fun FavoriteListScreen(
    modifier: Modifier = Modifier,
    state: List<Product>?,
    onEvent: (FavoriteScreenUIEvent) -> Unit
) {
    Scaffold(
        topBar = {
            FavoriteScreenTopAppBar(
                title = "Favorites"
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            if (state.isNullOrEmpty()) {
                NoFavoriteScreen()
            }
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(state.orEmpty(), key = { it.id }) { product ->
                    FavoriteListItem(
                        modifier = Modifier.animateItem(),
                        product = product,
                        onItemClick = {},
                        onFavoriteClick = {
                            onEvent(FavoriteScreenUIEvent.OnFavoriteClicked(it))
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun FavoriteListItem(
    modifier: Modifier = Modifier,
    product: Product,
    onItemClick: () -> Unit,
    onFavoriteClick: (productId: String) -> Unit
) {
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))

    ) {
        Row(
            modifier = Modifier.clickable {  },
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "Product Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(8.dp)
                    .size(84.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            ) {
                Text(text = product.brand, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = product.name, fontWeight = FontWeight.Thin, fontSize = 12.sp)
            }
            IconButton(
                onClick = { onFavoriteClick(product.id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Favorite Button",
                    tint = Color.Yellow
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreenTopAppBar(
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
fun NoFavoriteScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "No favorites yet")
    }
}