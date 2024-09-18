package com.yonasoft.itemlister.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yonasoft.itemlister.data.model.Item
@Composable
fun ItemScreen(viewModel: ItemViewModel = viewModel()) {
    val items = viewModel.items.collectAsState().value

    if (items.isEmpty()) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        ItemList(items = items)
    }
}

@Composable
fun ItemList(items: List<Item>) {
    LazyColumn {
        items(items) { item ->
            Column {
                Text(text = "List ID: ${item.listId}")
                Text(text = "Item Name: ${item.name ?: "Unnamed Item"}")
            }
        }
    }
}
