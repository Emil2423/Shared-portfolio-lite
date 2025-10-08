package com.emil.sharedportfoliolite.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TotalPortfolioCard(total: Double, totalValue: Double) {
    val totalGainLoss = totalValue - total
    val totalGainLossPercentage = if (total > 0) {
        (totalGainLoss / total) * 100
    } else 0.0
    val isPositive = totalGainLoss >= 0

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Total Portfolio Value",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
            Text(
                text = "$%.2f".format(totalValue),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Initial Investment",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$%.2f".format(total),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.4f),
                    modifier = Modifier.size(24.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Gain/Loss",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${if (isPositive) "+" else ""}$%.2f".format(totalGainLoss),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFEF5350)
                        )
                        Surface(
                            color = if (isPositive)
                                Color(0xFF4CAF50).copy(alpha = 0.2f)
                            else
                                Color(0xFFEF5350).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${if (isPositive) "+" else ""}%.1f%%".format(totalGainLossPercentage),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFEF5350),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

