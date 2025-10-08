package com.emil.sharedportfoliolite.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.emil.sharedportfoliolite.data.database.AppDatabase
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import com.emil.sharedportfoliolite.data.repository.AssetRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val assetRepository = AssetRepository()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _assets = MutableStateFlow<List<Asset>>(emptyList())
    val assets: StateFlow<List<Asset>> = _assets

    private val _realTimePrices = MutableStateFlow<Map<String, Double>>(emptyMap())
    val realTimePrices: StateFlow<Map<String, Double>> = _realTimePrices

    private val priceJobs = mutableMapOf<String, Job>()

    init {
        loadUsers()
        loadAssets()
    }

    fun startRealTimePriceUpdates(ticker: String, intervalMs: Long = 10000) {
        // Don't start if already running for this ticker
        if (priceJobs.containsKey(ticker)) return

        priceJobs[ticker] = viewModelScope.launch {
            while (true) {
                try {
                    val price = assetRepository.fetchAssetPrice(ticker)
                    if (price != null) {
                        val currentPrices = _realTimePrices.value.toMutableMap()
                        currentPrices[ticker] = price
                        _realTimePrices.value = currentPrices

                        // Update ONLY pricePerShareUSD (for offline display), NOT purchasePrice
                        val asset = _assets.value.find { it.ticker == ticker }
                        asset?.let {
                            val updatedAsset = it.copy(pricePerShareUSD = price)
                            updateAsset(updatedAsset)
                        }
                    }
                } catch (e: Exception) {
                    // Remove price on error
                    val currentPrices = _realTimePrices.value.toMutableMap()
                    currentPrices.remove(ticker)
                    _realTimePrices.value = currentPrices
                }
                delay(intervalMs)
            }
        }
    }

    fun stopRealTimePriceUpdates(ticker: String) {
        priceJobs[ticker]?.cancel()
        priceJobs.remove(ticker)
        val currentPrices = _realTimePrices.value.toMutableMap()
        currentPrices.remove(ticker)
        _realTimePrices.value = currentPrices
    }

    private fun stopAllRealTimePriceUpdates() {
        priceJobs.values.forEach { it.cancel() }
        priceJobs.clear()
        _realTimePrices.value = emptyMap()
    }

    fun calculateUserCurrentValue(user: User, assets: List<Asset>, realTimePrices: Map<String, Double>): Double {
        val totalContributions = _users.value.sumOf { it.contributionUSD }
        if (totalContributions == 0.0) return 0.0

        val userPercentage = user.contributionUSD / totalContributions

        return assets.sumOf { asset ->
            val currentPrice = realTimePrices[asset.ticker] ?: asset.pricePerShareUSD
            val userShares = asset.sharesOwned * userPercentage
            userShares * currentPrice
        }
    }

    fun calculateTotalPortfolioValue(assets: List<Asset>, realTimePrices: Map<String, Double>): Double {
        return assets.sumOf { asset ->
            val currentPrice = realTimePrices[asset.ticker] ?: asset.pricePerShareUSD
            asset.sharesOwned * currentPrice
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            database.userDao().getAllUsers().collect { userList ->
                _users.value = userList
            }
        }
    }

    private fun loadAssets() {
        viewModelScope.launch {
            database.assetDao().getAllAssets().collect { assetList ->
                _assets.value = assetList
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            database.userDao().insert(user)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            database.userDao().update(user)
        }
    }

    fun addAsset(asset: Asset) {
        viewModelScope.launch {
            database.assetDao().insert(asset)
        }
    }

    fun updateAsset(asset: Asset) {
        viewModelScope.launch {
            database.assetDao().update(asset)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            database.userDao().delete(user)
        }
    }

    fun deleteAsset(asset: Asset) {
        viewModelScope.launch {
            stopRealTimePriceUpdates(asset.ticker)
            database.assetDao().delete(asset)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopAllRealTimePriceUpdates()
    }
}
