package com.example.womensafetyapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.womensafetyapp.ui.theme.*

@Composable
fun QuickActionCard(title: String) {

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = title, fontSize = 16.sp)
        }
    }
}