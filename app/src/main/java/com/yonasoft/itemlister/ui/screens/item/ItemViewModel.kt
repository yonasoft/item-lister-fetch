package com.yonasoft.itemlister.ui.screens.item

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonasoft.itemlister.data.model.Item
import com.yonasoft.itemlister.data.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemViewModel : ViewModel() {
    private val repository = ItemRepository()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val currentItems = mutableStateOf(emptyList<Item>())

    init {
        initializeItems()
    }

    private fun initializeItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getItems()
            _items.value = result

            withContext(Dispatchers.Default) {
                searchSortAndFilter(searchInput = "", isSort = true, isFilter = true)
            }
        }
    }

    fun launchSearchSortAndFilter(searchInput: String, isSort: Boolean, isFilter: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            searchSortAndFilter(searchInput = searchInput, isSort = isSort, isFilter = isFilter)
        }
    }

    private fun searchSortAndFilter(searchInput: String, isSort: Boolean, isFilter: Boolean) {
        var updatedItems = _items.value

        if (searchInput.isNotEmpty())
            updatedItems = updatedItems.filter {
                it.name?.contains(
                    searchInput,
                    ignoreCase = true
                ) == true
            }
        if (isFilter)
            updatedItems = updatedItems.filter { !it.name.isNullOrBlank() }
        if (isSort)
//          updatedItems = updatedItems.sortedWith(compareBy({ it.listId }, { it.name }))
            updatedItems = updatedItems.sortedWith(compareBy({ it.listId }, {
                it.name?.drop(5)
                    ?.toInt()
            }))
        currentItems.value = updatedItems
    }
}