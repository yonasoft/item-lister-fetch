@file:OptIn(ExperimentalMaterial3Api::class)

package com.yonasoft.itemlister.ui.screens.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yonasoft.itemlister.data.model.Item

@Composable
fun ItemScreen(viewModel: ItemViewModel = viewModel()) {
    val items = viewModel.currentItems.value

    var searchInput by rememberSaveable { mutableStateOf("") }
    var isSortedByName by rememberSaveable { mutableStateOf(true) }
    var filterOutNullAndEmpty by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(searchInput, isSortedByName, filterOutNullAndEmpty) {
        viewModel.searchAndApplySortAndFilter(
            searchInput = searchInput,
            isSort = isSortedByName,
            isFilter = filterOutNullAndEmpty
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SearchBar(
            query = searchInput,
            onQueryChange = { searchInput = it },
            onSearch = {
                //Handled in launch effect
            },
            active = false,
            onActiveChange = {}) {
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (items.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            } else {
                val groupedItems = items
                    .groupBy { it.listId }
                    .toSortedMap()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    state = rememberLazyListState()
                ) {
                    groupedItems.forEach { (listId, groupItems) ->
                        item {
                            ExpandableItemGroup(
                                listId = listId,
                                items = if (isSortedByName) groupItems else groupItems
                            )
                        }
                    }
                }
            }
        }
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        SortingAndFilteringControls(
            isSortedByName = isSortedByName,
            filterOutNullAndEmpty = filterOutNullAndEmpty,
            onSortChange = { isSortedByName = it },
            onFilterChange = { filterOutNullAndEmpty = it }
        )
    }
}

@Composable
fun ExpandableItemGroup(listId: Int, items: List<Item>) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = "List ID: $listId"
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }

        Divider()

        if (expanded) {
            items.forEach { item ->
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "Item Name: ${item.name}")
                    Divider()
                }
            }
        }
    }
}

@Composable
fun SortingAndFilteringControls(
    isSortedByName: Boolean,
    filterOutNullAndEmpty: Boolean,
    onSortChange: (Boolean) -> Unit,
    onFilterChange: (Boolean) -> Unit
) {
    Column {

        Text("Sorting and Filtering Options")

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isSortedByName,
                onCheckedChange = { onSortChange(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sort by Name")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = filterOutNullAndEmpty,
                onCheckedChange = { onFilterChange(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Filter out empty or null names")
        }
    }
}


