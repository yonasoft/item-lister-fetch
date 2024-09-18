package com.yonasoft.itemlister.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonasoft.itemlister.data.model.Item
import com.yonasoft.itemlister.data.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    init {
        fetchItems()
    }

    private fun fetchItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFilteredItems()
            _items.value = result
        }
    }
}
