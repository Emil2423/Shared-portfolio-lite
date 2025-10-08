package com.emil.sharedportfoliolite.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import com.emil.sharedportfoliolite.viewmodels.PortfolioViewModel

@Composable
fun AssetCard(
    asset: Asset,
    users: List<User>,
    totalPortfolio: Double,
    viewModel: PortfolioViewModel,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val realTimePrices by viewModel.realTimePrices.collectAsState()
    val displayPrice = realTimePrices[asset.ticker] ?: asset.pricePerShareUSD
    val isLive = realTimePrices.containsKey(asset.ticker)

    val priceChange = displayPrice - asset.purchasePrice
    val priceChangePercentage = if (asset.purchasePrice > 0) {
        (priceChange / asset.purchasePrice) * 100
    } else 0.0
    val isPricePositive = priceChange >= 0

    LaunchedEffect(asset.ticker) {
        viewModel.startRealTimePriceUpdates(asset.ticker, intervalMs = 10000)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = asset.ticker,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${asset.sharesOwned} shares",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (isLive) {
                            Text(
                                text = "• Live",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Price Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Purchase",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$%.2f".format(asset.purchasePrice),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Current",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$%.2f".format(displayPrice),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (displayPrice != asset.purchasePrice) {
                    Surface(
                        color = if (isPricePositive)
                            Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else
                            Color(0xFFEF5350).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "${if (isPricePositive) "+" else ""}$%.2f".format(priceChange),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPricePositive) Color(0xFF4CAF50) else Color(0xFFEF5350)
                            )
                            Text(
                                text = "${if (isPricePositive) "+" else ""}%.1f%%".format(priceChangePercentage),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isPricePositive) Color(0xFF4CAF50) else Color(0xFFEF5350)
                            )
                        }
                    }
                }
            }

            // User shares
            if (users.isNotEmpty()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Share Distribution",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    users.forEach { user ->
                        val userPercentage = if (totalPortfolio > 0) {
                            user.contributionUSD / totalPortfolio
                        } else 0.0
                        val userShare = asset.sharesOwned * userPercentage

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = user.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "%.4f shares".format(userShare),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

