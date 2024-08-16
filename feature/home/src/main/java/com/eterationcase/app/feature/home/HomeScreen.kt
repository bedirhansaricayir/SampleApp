package com.eterationcase.app.feature.home

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.eterationcase.app.core.model.Product
import com.eterationcase.app.feature.home.components.FilterBottomSheet

/**
 * Created by bedirhansaricayir on 14.08.2024
 */

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val homeUIState: HomeScreenUIState by viewModel.state.collectAsStateWithLifecycle()
    val selectedBrands by viewModel.selectedBrands.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val homeScreenUIEffect = viewModel.effect

    LaunchedEffect(homeScreenUIEffect) {
        homeScreenUIEffect.collect { effect ->
            when (effect) {
                is HomeScreenUIEffect.NavigateToDetail -> navigateToDetail(effect.productId)
            }
        }
    }

    Scaffold(
        topBar = {
            HomeScreenTopBar(title = "E-Market")
        }
    ) {
        HomeScreenContent(
            modifier = modifier.padding(it),
            state = homeUIState,
            products = products,
            selectedBrands = selectedBrands,
            onEvent = viewModel::setEvent
        )
    }
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeScreenUIState,
    products: List<Product>?,
    selectedBrands: List<String>,
    onEvent: (HomeScreenUIEvent) -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                onSearchQueryChanged = { onEvent(HomeScreenUIEvent.OnSearchQueryChanged(it)) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    is HomeScreenUIState.Error -> ErrorScreen(
                        errorMessage = state.message ?: "Something Went Wrong!"
                    )

                    HomeScreenUIState.Loading -> LoadingScreen()
                    is HomeScreenUIState.Success -> ListScreen(
                        state = state,
                        products = products,
                        selectedBrands = selectedBrands,
                        onEvent = onEvent
                    )
                }
            }
        }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenUIState.Success,
    products: List<Product>?,
    selectedBrands: List<String>,
    onEvent: (HomeScreenUIEvent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }
    val sortByOptionList = listOf(
        "Old to new",
        "New to old",
        "Price high to low",
        "Price low to high"
    )
    var selectedSortByIndex by remember { mutableIntStateOf(0) }

    if (products.isNullOrEmpty()) {
        ProductNotFound()
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Filters:")
            ElevatedButton(
                modifier = Modifier.padding(horizontal = 8.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color.LightGray,
                    contentColor = Color.Black.copy(alpha = 0.5f)
                ),
                onClick = { showSheet = true }
            ) {
                Text(text = "Select Filter")
            }
        }
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(products.orEmpty(), key = { it.id }) { product ->
                ProductItem(
                    modifier = Modifier.animateItem(),
                    product = product,
                    onItemClick = { id ->
                        onEvent(HomeScreenUIEvent.OnProductClick(id))
                    },
                    onAddToCardClick = { onEvent(HomeScreenUIEvent.OnAddToCardClick(it)) },
                    onFavoriteClick = { id, isFavorite ->
                        onEvent(HomeScreenUIEvent.OnFavoriteClick(id,isFavorite))
                    }
                )
            }
        }
    }

    FilterBottomSheet(
        sheetState = bottomSheetState,
        onEvent = onEvent,
        show = showSheet,
        isFilterApplying = state.isFilterApplying,
        brandList = state.brandList.orEmpty(),
        onDismissRequest = { showSheet = false },
        selectedBrands = selectedBrands,
        sortByOptionList = sortByOptionList,
        selectedSortByItem = sortByOptionList[selectedSortByIndex],
        onSelectedSortByIndex = { selectedSortByIndex = it }
    )
}

@Composable
fun ProductNotFound(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Product Not Found")
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onItemClick: (id: String) -> Unit,
    onAddToCardClick: (productId: String) -> Unit,
    onFavoriteClick: (productId: String, isFavorite: Boolean) -> Unit
) {
    val density = LocalDensity.current.density

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onItemClick(product.id) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column {
        Box(
            modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = product.image,
                contentDescription = "Product Image",
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd),
                onClick = { onFavoriteClick(product.id,!product.isFavorite) }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Favorite Button",
                    tint = if (product.isFavorite) Color.Yellow else Color.LightGray
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = "${product.price} ₺",
            color = Color(0xFF2A59FE)
        )
        var padding by remember { mutableStateOf(0.dp) }

        Text(
            modifier = Modifier,
            text = product.name,
            color = Color.Black,
            onTextLayout = {
                val lineCount = it.lineCount
                val height = (it.size.height / density).dp
                println("lineCount: $lineCount, Height: $height")
                padding = if (lineCount > 1) 0.dp else height
            }
        )
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF2A59FE),
                contentColor = Color.White
            ),
            onClick = { onAddToCardClick(product.id) }
        ) {
            Text(text = "Add to Cart")
        }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    modifier: Modifier = Modifier,
    title: String
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
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@Composable
fun SearchBar(
    onSearchQueryChanged: (String) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    var text by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        value = text,
        onValueChange = {
            onSearchQueryChanged(it)
            text = it
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboard?.hide()
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Button",
                tint = Color.LightGray
            )
        },
        placeholder = {
            Text(text = "Search", color = Color.LightGray)
        },
        trailingIcon = {
            if (text.isNotBlank()) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            text = ""
                            onSearchQueryChanged("")
                        },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
        )
    )
}