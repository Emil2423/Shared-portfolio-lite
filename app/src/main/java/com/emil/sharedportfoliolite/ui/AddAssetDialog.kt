package com.emil.sharedportfoliolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddAssetDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Double) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var purchasePrice by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Asset") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it.uppercase() },
                    label = { Text("Asset Symbol (e.g., AAPL)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Quantity (Shares)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = purchasePrice,
                    onValueChange = { purchasePrice = it },
                    label = { Text("Purchase Price per Share ($)") },
                    supportingText = { Text("Enter the price you bought it at") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = quantity.toDoubleOrNull()
                    val prc = purchasePrice.toDoubleOrNull()
                    if (name.isNotBlank() && qty != null && qty > 0 && prc != null && prc > 0) {
                        onConfirm(name.uppercase(), qty, prc)
                        onDismiss()
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
