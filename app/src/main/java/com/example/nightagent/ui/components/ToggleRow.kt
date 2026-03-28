package com.example.nightagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onToggle: (Boolean) -> Unit
) {

    var checked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(title)
                Text(subtitle)
            }

            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onToggle(it)
                }
            )
        }
    }
}