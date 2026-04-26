package com.example.nightagent.repository

import com.example.nightagent.firebase.AuthManager
import com.example.nightagent.firebase.FirebaseConfig
import com.example.nightagent.model.SOSModel
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SOSRepository {
    
    suspend fun sendSOS(data: SOSModel): Result<String> = withContext(Dispatchers.IO) {
    try {
        val docRef = FirebaseConfig.firestore
            .collection("sos_logs")
            .add(data)
            .await()

        Result.success(docRef.id) // 🔥 return ID

    } catch (e: Exception) {
        Result.failure(e)
    }
}
    
    suspend fun getCurrentUserId(): String {
        return AuthManager.getCurrentUserId() ?: throw IllegalStateException("User not logged in")
    }
}
