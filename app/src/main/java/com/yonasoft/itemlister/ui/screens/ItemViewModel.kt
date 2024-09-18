package com.yonasoft.itemlister.ui.screens

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
    }
