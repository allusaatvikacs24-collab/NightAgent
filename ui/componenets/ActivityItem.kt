package com.example.nightagent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActivityItem(title: String, time: String) {

    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = title)
        Text(text = time)
    }
}