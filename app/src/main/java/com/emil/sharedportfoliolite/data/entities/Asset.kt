package com.emil.sharedportfoliolite.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assets")
data class Asset (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ticker: String,
    val purchasePrice: Double,  // The price you bought it at (never changes)
    val pricePerShareUSD: Double,  // Current/last known price (for display when offline)
    val sharesOwned: Double
)
