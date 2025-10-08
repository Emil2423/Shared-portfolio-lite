package com.emil.sharedportfoliolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.emil.sharedportfoliolite.data.entities.Asset

@Composable
fun EditAssetDialog(
    asset: Asset,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var quantity by remember { mutableStateOf(asset.sharesOwned.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit ${asset.ticker} Quantity") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (Shares)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Current: %.4f shares".format(asset.sharesOwned),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Price per share: $%.2f".format(asset.pricePerShareUSD),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    quantity.toDoubleOrNull()?.let { amount ->
                        if (amount >= 0) {
                            onConfirm(amount)
                        }
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

