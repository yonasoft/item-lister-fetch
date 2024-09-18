package com.yonasoft.itemlister.data.repository

import com.yonasoft.itemlister.data.api.RetrofitInstance
import com.yonasoft.itemlister.data.model.Item

class ItemRepository {
    suspend fun getFilteredItems(): List<Item> {
        val response = RetrofitInstance.api.getItems()

        if (response.isSuccessful) {
            response.body()?.let { items ->
                return items.filter { !it.name.isNullOrBlank() }
                    .sortedWith(compareBy({ it.listId }, { it.name }))
            }
        }

        return emptyList()
    }
}