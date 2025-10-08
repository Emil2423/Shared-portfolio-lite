package com.emil.sharedportfoliolite.data.repository

import com.emil.sharedportfoliolite.api.YahooFinanceApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AssetRepository {
    private val apiService = YahooFinanceApiService.create()

    suspend fun fetchAssetPrice(symbol: String): Double? = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.getQuote(symbol)
            response.chart.result?.firstOrNull()?.meta?.regularMarketPrice
        } catch (e: Exception) {
            null
        }
    }
}