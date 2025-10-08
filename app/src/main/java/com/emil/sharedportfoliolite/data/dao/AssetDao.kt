package com.emil.sharedportfoliolite.data.dao


import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emil.sharedportfoliolite.data.entities.Asset

@Dao
interface AssetDao {
    @Insert
    suspend fun insert(asset: Asset)

    @Query("SELECT * FROM assets")
    fun getAllAssets(): Flow<List<Asset>>

    @Update
    suspend fun update(asset: Asset)

    @Delete
    suspend fun delete(asset: Asset)
}