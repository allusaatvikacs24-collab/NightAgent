package com.example.nightagent.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SOSModel(
    val userId: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long
) : Parcelable
