package com.yonasoft.itemlister.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yonasoft.itemlister.data.model.Item

@Composable
fun ItemScreen(viewModel: ItemViewModel = viewModel()) {
    val items = viewModel.currentItems.value

    var isSortedByName by remember { mutableStateOf(true) }
    var filterOutNullAndEmpty by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (items.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.size(64.dp))
            } else {
                val groupedItems = items
                    .filter { !filterOutNullAndEmpty || !it.name.isNullOrBlank() }
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
                                items = if (isSortedByName) groupItems.sortedBy { it.name } else groupItems
                            )
                        }
                    }
                }
            }
        }
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
                    Text(text = "Item Name: ${item.name ?: "Unnamed Item"}")
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

@Composable
fun Divider() {
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Surface(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)) {}
    }
}
