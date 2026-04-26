package com.example.nightagent.repository

import com.example.nightagent.firebase.FirebaseConfig
import com.example.nightagent.model.SafeWalkModel
import com.example.nightagent.model.LocationModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SafeWalkRepository {
    
    suspend fun startSafeWalk(userId: String, startTime: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val model = SafeWalkModel(userId, startTime, true)
            FirebaseConfig.firestore
                .collection("safe_walks")
                .document(userId)
                .set(model)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateLocation(userId: String, location: LocationModel): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            FirebaseConfig.firestore
                .collection("safe_walks")
                .document(userId)
                .collection("locations")
                .document()
                .set(location)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun endSafeWalk(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            FirebaseConfig.firestore
                .collection("safe_walks")
                .document(userId)
                .update("isActive", false)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
