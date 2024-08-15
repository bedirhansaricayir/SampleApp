package com.eterationcase.app.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eterationcase.app.feature.home.HomeScreenUIEvent

/**
 * Created by bedirhansaricayir on 15.08.2024
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onEvent: (HomeScreenUIEvent) -> Unit,
    show: Boolean = false,
    isFilterApplying: Boolean,
    brandList: List<String>,
    selectedBrands: List<String>,
    sortByOptionList: List<String>,
    selectedSortByItem: String,
    onSelectedSortByIndex: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {


    if (show) {
        ModalBottomSheet(
            modifier = modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            dragHandle = {
                FilterBottomSheetTitle(
                    onCloseClick = onDismissRequest
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(4.dp),
                            text = "Sort By",
                            color = Color.Gray
                        )
                        if (isFilterApplying) {
                            TextButton(
                                onClick = {
                                    onEvent(HomeScreenUIEvent.OnClearFilter)
                                    onDismissRequest()
                                }
                            ) {
                                Text(text = "Clear Filter")
                            }
                        }
                    }
                    SelectableSortByGroup(
                        sortByList = sortByOptionList,
                        selectedItem = selectedSortByItem,
                        onItemSelected = { selectedItem ->
                            onSelectedSortByIndex(sortByOptionList.indexOf(selectedItem))
                            //selectedSortByIndex = sortByOptionList.indexOf(selectedItem)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    )

                    Text(modifier = Modifier.padding(4.dp), text = "Brand", color = Color.Gray)
                    SelectableBrandGroup(
                        brandList = brandList,
                        selectedItem = selectedBrands,
                        onItemSelected = { onEvent(HomeScreenUIEvent.OnBrandSelected(it)) }
                    )
                }

                ElevatedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color(0xFF2A59FE),
                        contentColor = Color.White
                    ),
                    onClick = {
                        onEvent(
                            HomeScreenUIEvent.OnApplyFilter(
                                selectedSortByItem,
                                selectedBrands
                            )
                        )
                        onDismissRequest()
                    }
                ) {
                    Text(text = "Primary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheetTitle(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        title = {
            Text(text = "Filter")
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Button")
            }
        }
    )
}

@Composable
fun SelectableSortByGroup(
    modifier: Modifier = Modifier,
    sortByList: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        sortByList.forEachIndexed { index, item ->
            SelectableSortByItem(
                option = item,
                isSelected = item == selectedItem,
                onSelect = onItemSelected
            )
        }
    }
}

@Composable
fun SelectableBrandGroup(
    modifier: Modifier = Modifier,
    brandList: List<String>,
    selectedItem: List<String>,
    onItemSelected: (String) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    val filteredBrandList = brandList.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

    TextField(
        value = searchQuery,
        onValueChange = { newQuery ->
            searchQuery = newQuery
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
            if (searchQuery.isNotBlank()) {
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable {
                            searchQuery = ""
                        },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
        )
    )

    LazyColumn(
        modifier = modifier
    ) {
        items(filteredBrandList) { item ->
            SelectableBrandItem(
                option = item,
                isSelected = selectedItem.contains(item),
                onSelect = onItemSelected
            )
        }
    }
}


@Composable
fun SelectableSortByItem(
    modifier: Modifier = Modifier,
    option: String,
    isSelected: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(option) }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(option) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF2A59FE),
                unselectedColor = Color(0xFF2A59FE)
            )
        )
        Text(text = option)
    }
}

@Composable
fun SelectableBrandItem(
    modifier: Modifier = Modifier,
    option: String,
    isSelected: Boolean,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(option) }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = {
                onSelect(option)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF2A59FE),
                uncheckedColor = Color(0xFF2A59FE)
            )
        )
        Text(text = option)
    }
}