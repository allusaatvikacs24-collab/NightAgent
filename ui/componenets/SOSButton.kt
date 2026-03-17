package com.example.nightagent.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SOSButton() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(180.dp)
                .background(Color(0xFFAA336A), CircleShape),
            contentAlignment = Alignment.Center
        ) {

            Text(
                "SOS",
                color = Color.White,
                fontSize = 32.sp
            )

        }

    }

}