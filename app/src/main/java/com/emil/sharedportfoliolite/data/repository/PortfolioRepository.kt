package com.emil.sharedportfoliolite.data.repository

import com.emil.sharedportfoliolite.data.dao.AssetDao
import com.emil.sharedportfoliolite.data.dao.UserDao
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User


class PortfolioRepository(
    private val userDao: UserDao,
    private val assetDao: AssetDao
) {
    suspend fun insertUser(user: User) = userDao.insert(user)
    suspend fun getAllUsers(): List<User> = userDao.getAllUsers()
    suspend fun deleteUser(user: User) = userDao.delete(user)

    suspend fun insertAsset(asset: Asset) = assetDao.insert(asset)
    suspend fun getAllAssets(): List<Asset> = assetDao.getAllAssets()
    suspend fun updateAsset(asset: Asset) = assetDao.update(asset)
    suspend fun deleteAsset(asset: Asset) = assetDao.delete(asset)
}