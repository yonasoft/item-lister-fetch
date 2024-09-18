package com.yonasoft.itemlister.data.api

import com.yonasoft.itemlister.data.model.Item
import retrofit2.Response
import retrofit2.http.GET

interface ItemApiService {
    @GET("hiring.json")
    suspend fun getItems(): Response<List<Item>>
}
