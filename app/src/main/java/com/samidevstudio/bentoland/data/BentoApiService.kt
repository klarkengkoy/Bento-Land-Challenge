package com.samidevstudio.bentoland.data

import retrofit2.http.GET

interface BentoApiService {
    @GET("menu.json")
    suspend fun getMenu(): MenuResponse
}
