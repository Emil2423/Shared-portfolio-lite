package com.emil.sharedportfoliolite.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ticker: String,
    val pricePerShareUSD: Double,
    val sharesOwned: Double
)
