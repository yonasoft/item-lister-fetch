package com.yonasoft.itemlister.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonasoft.itemlister.data.model.Item
import com.yonasoft.itemlister.data.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    var currentItems = mutableStateOf(emptyList<Item>())

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getItems()
            _items.value = result
            currentItems.value = result
        }
    }

    fun applySortAndFilter(isSort: Boolean, isFilter: Boolean) {
        val items = _items.value
        viewModelScope.launch(Dispatchers.Default) {
            var updatedItems = items
            if (isSort) updatedItems = sortItems(updatedItems)
            if (isFilter) updatedItems = filterNullAndEmptyItems(updatedItems)
            currentItems.value = updatedItems
        }
    }
    private fun sortItems(items: List<Item>): List<Item> {
        return items.sortedWith(compareBy({ it.listId }, { it.name }))
    }

    private  fun filterNullAndEmptyItems(items: List<Item>): List<Item> {
        return items.filter { !it.name.isNullOrBlank() }
    }
}

