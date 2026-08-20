package com.samidevstudio.bentoland.data

class MenuRepository(private val apiService: BentoApiService) {
    suspend fun fetchMenu(): Result<List<MenuItem>> {
        return try {
            val response = apiService.getMenu()
            Result.success(response.items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
