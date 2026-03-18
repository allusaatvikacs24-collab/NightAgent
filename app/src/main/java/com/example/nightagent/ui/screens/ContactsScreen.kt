package com.example.nightagent.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nightagent.ui.theme.*

data class EmergencyContact(val name: String, val phone: String, val isPriority: Boolean)

@Composable
fun ContactsScreen() {
    val contacts = listOf(
        EmergencyContact("Mom", "+1 234 567 890", true),
        EmergencyContact("Dad", "+1 234 567 891", false),
        EmergencyContact("Sister", "+1 234 567 892", false),
        EmergencyContact("Best Friend", "+1 234 567 893", true)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PastelBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "Emergency Contacts",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Primary contact highlighted",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(contacts.size) { index ->
                ContactCard(contacts[index])
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Add Contact FAB
        FloatingActionButton(
            onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp),
            containerColor = BlushPink,
            contentColor = PurpleEnd
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Contact")
        }
    }
}

@Composable
fun ContactCard(contact: EmergencyContact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Initial
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        if (contact.isPriority) BlushPink.copy(alpha = 0.3f) 
                        else Lavender.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.name.take(1).uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = if (contact.isPriority) PurpleEnd else Lavender
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = contact.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextPrimary
                    )
                    if (contact.isPriority) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Primary",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = contact.phone,
                    color = TextSecondary,
                    fontSize = 15.sp
                )
            }
            
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .background(SuccessGreen.copy(alpha = 0.15f), CircleShape)
            ) {
                Icon(Icons.Default.Call, null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
            }
        }
    }
}
