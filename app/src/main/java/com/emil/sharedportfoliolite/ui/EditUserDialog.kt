package com.emil.sharedportfoliolite.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.emil.sharedportfoliolite.data.entities.User

@Composable
fun EditUserDialog(
    user: User,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var contribution by remember { mutableStateOf(user.contributionUSD.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit ${user.name}'s Contribution") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = contribution,
                    onValueChange = { contribution = it },
                    label = { Text("Contribution (USD)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Current: $%.2f".format(user.contributionUSD),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    contribution.toDoubleOrNull()?.let { amount ->
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

