package com.yonasoft.itemlister.ui.screens.item

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
    fun searchAndApplySortAndFilter(searchInput: String, isSort: Boolean, isFilter: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            var updatedItems = _items.value

            if (searchInput.isNotEmpty()) updatedItems = updatedItems.filter { it.name?.contains(searchInput, ignoreCase = true) == true }
            if (isFilter) updatedItems = updatedItems.filter { !it.name.isNullOrBlank() }
            if (isSort) updatedItems = updatedItems.sortedWith(compareBy({ it.listId }, { it.name }))

            currentItems.value = updatedItems
        }
    }
}