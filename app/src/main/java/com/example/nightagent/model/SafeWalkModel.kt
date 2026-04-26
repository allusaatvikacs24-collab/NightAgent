package com.example.nightagent.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SafeWalkModel(
    val userId: String,
    val startTime: Long,
    val isActive: Boolean
) : Parcelable
