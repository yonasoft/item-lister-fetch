package com.yonasoft.itemlister.data.repository

import android.util.Log
import com.yonasoft.itemlister.data.api.RetrofitInstance
import com.yonasoft.itemlister.data.model.Item

class ItemRepository {

    suspend fun getItems(): List<Item> {
        val response = RetrofitInstance.api.getItems()

        if (response.isSuccessful) {
            response.body()?.let { items ->
                Log.i("items", items.toString())
                return items
            }
        }
        return emptyList()
    }
}