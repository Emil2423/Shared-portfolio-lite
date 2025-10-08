package com.emil.sharedportfoliolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.emil.sharedportfoliolite.data.entities.Asset
import com.emil.sharedportfoliolite.data.entities.User
import com.emil.sharedportfoliolite.viewmodels.PortfolioViewModel
import com.emil.sharedportfoliolite.ui.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioScreen(
    modifier: Modifier = Modifier,
    viewModel: PortfolioViewModel = viewModel()
) {
    val users by viewModel.users.collectAsState()
    val assets by viewModel.assets.collectAsState()
    val realTimePrices by viewModel.realTimePrices.collectAsState()

    val totalPortfolio = users.sumOf { it.contributionUSD }
    val initialInvestment = assets.sumOf { asset ->
        asset.sharesOwned * asset.purchasePrice
    }
    val totalPortfolioValue = viewModel.calculateTotalPortfolioValue(assets, realTimePrices)

    var showUserDialog by remember { mutableStateOf(false) }
    var showAssetDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<User?>(null) }
    var editingAsset by remember { mutableStateOf<Asset?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Shared Portfolio",
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FloatingActionButton(
                    onClick = { showAssetDialog = true },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(Icons.Default.Add, "Add Asset")
                        Text("Stock", fontSize = 10.sp)
                    }
                }
                FloatingActionButton(
                    onClick = { showUserDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    elevation = FloatingActionButtonDefaults.elevation(6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(Icons.Default.Person, "Add User")
                        Text("User", fontSize = 10.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item { TotalPortfolioCard(initialInvestment, totalPortfolioValue) }

            if (users.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Team Members",
                        subtitle = "${users.size} member${if (users.size != 1) "s" else ""}"
                    )
                }
                items(users) { user ->
                    val userPercentage = if (totalPortfolio > 0) {
                        user.contributionUSD / totalPortfolio
                    } else 0.0
                    val userInitialInvestment = assets.sumOf { asset ->
                        asset.sharesOwned * asset.purchasePrice * userPercentage
                    }

                    UserCard(
                        user = user,
                        totalPortfolio = totalPortfolio,
                        initialInvestment = userInitialInvestment,
                        currentValue = viewModel.calculateUserCurrentValue(user, assets, realTimePrices),
                        onEdit = { editingUser = user },
                        onDelete = { viewModel.deleteUser(user) }
                    )
                }
            }

            if (assets.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Portfolio Assets",
                        subtitle = "${assets.size} asset${if (assets.size != 1) "s" else ""}"
                    )
                }
                items(assets) { asset ->
                    AssetCard(
                        asset = asset,
                        users = users,
                        totalPortfolio = totalPortfolio,
                        viewModel = viewModel,
                        onEdit = { editingAsset = asset },
                        onDelete = { viewModel.deleteAsset(asset) }
                    )
                }
            }

            if (users.isEmpty() && assets.isEmpty()) {
                item { EmptyState() }
            }
        }
    }

    if (showUserDialog) {
        AddUserDialog(
            onDismiss = { showUserDialog = false },
            onConfirm = { name, contribution ->
                viewModel.addUser(User(name = name, contributionUSD = contribution))
            }
        )
    }

    if (showAssetDialog) {
        AddAssetDialog(
            onDismiss = { showAssetDialog = false },
            onConfirm = { name, quantity, price ->
                viewModel.addAsset(
                    Asset(
                        ticker = name,
                        sharesOwned = quantity,
                        purchasePrice = price,
                        pricePerShareUSD = price
                    )
                )
            }
        )
    }

    editingUser?.let { user ->
        EditUserDialog(
            user = user,
            onDismiss = { editingUser = null },
            onConfirm = { updatedContribution ->
                viewModel.updateUser(user.copy(contributionUSD = updatedContribution))
                editingUser = null
            }
        )
    }

    editingAsset?.let { asset ->
        EditAssetDialog(
            asset = asset,
            onDismiss = { editingAsset = null },
            onConfirm = { updatedQuantity ->
                viewModel.updateAsset(asset.copy(sharesOwned = updatedQuantity))
                editingAsset = null
            }
        )
    }
}
