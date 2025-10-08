package com.emil.sharedportfoliolite.data.repository

import com.emil.sharedportfoliolite.api.YahooFinanceApiService
import com.emil.sharedportfoliolite.data.dao.AssetDao
import com.emil.sharedportfoliolite.data.dao.UserDao
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

class PortfolioRepository(
    private val userDao: UserDao,
    private val assetDao: AssetDao,
    private val yahooFinanceApi: YahooFinanceApiService
) {
    suspend fun insertUser(user: User) = userDao.insert(user)
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun insertAsset(asset: Asset) = assetDao.insert(asset)
    fun getAllAssets(): Flow<List<Asset>> = assetDao.getAllAssets()
    suspend fun updateAsset(asset: Asset) = assetDao.update(asset)
    suspend fun deleteAsset(asset: Asset) = assetDao.delete(asset)

    fun getRealTimePrice(symbol: String): Flow<Double?> = flow {
        while (true) {
            try {
                val response = yahooFinanceApi.getQuote(symbol)
                val price = response.chart?.result?.firstOrNull()?.meta?.regularMarketPrice
                emit(price)
            } catch (e: Exception) {
                emit(null)
            }
            delay(5000) // Update every 5 seconds
        }
    }
}
