package com.emil.sharedportfoliolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import com.emil.sharedportfoliolite.viewmodels.PortfolioViewModel

@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    viewModel: PortfolioViewModel = viewModel()
) {
    val users by viewModel.users.observeAsState(emptyList())
    val assets by viewModel.assets.observeAsState(emptyList())

    val totalPortfolio = users.sumOf { it.contributionUSD }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            TotalPortfolioCard(totalPortfolio)
        }

        item {
            Text(
                text = "Users",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(users) { user ->
            UserCard(user, totalPortfolio)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Assets",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(assets) { asset ->
            AssetCard(asset, users, totalPortfolio)
        }
    }
}

@Composable
fun TotalPortfolioCard(total: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Total Portfolio Value",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$%.2f".format(total),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun UserCard(user: User, totalPortfolio: Double) {
    val percentage = if (totalPortfolio > 0) {
        (user.contributionUSD / totalPortfolio) * 100
    } else {
        0.0
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$%.2f".format(user.contributionUSD),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "%.1f%%".format(percentage),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AssetCard(asset: Asset, users: List<User>, totalPortfolio: Double) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = asset.ticker,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Quantity: ${asset.sharesOwned} @ $%.2f".format(asset.pricePerShareUSD),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            users.forEach { user ->
                val userPercentage = if (totalPortfolio > 0) {
                    user.contributionUSD / totalPortfolio
                } else {
                    0.0
                }
                val userShare = asset.sharesOwned * userPercentage

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = user.name, fontSize = 14.sp)
                    Text(
                        text = "$.4f units".format(userShare),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}