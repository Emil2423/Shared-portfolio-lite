package com.emil.sharedportfoliolite.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emil.sharedportfoliolite.data.database.AppDatabase
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import com.emil.sharedportfoliolite.data.repository.PortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PortfolioRepository

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _assets = MutableLiveData<List<Asset>>()
    val assets: LiveData<List<Asset>> get() = _assets

    init {
        val database = AppDatabase.getDatabase(application)
        repository = PortfolioRepository(database.userDao(), database.assetDao())
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _users.value = repository.getAllUsers()
            _assets.value = repository.getAllAssets()
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
            loadData()
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
            loadData()
        }
    }

    fun addAsset(asset: Asset) {
        viewModelScope.launch {
            repository.insertAsset(asset)
            loadData()
        }
    }

    fun updateAsset(asset: Asset) {
        viewModelScope.launch {
            repository.updateAsset(asset)
            loadData()
        }
    }

    fun deleteAsset(asset: Asset) {
        viewModelScope.launch {
            repository.deleteAsset(asset)
            loadData()
        }
    }


}