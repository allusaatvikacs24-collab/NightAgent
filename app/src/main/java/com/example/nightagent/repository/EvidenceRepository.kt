package com.example.nightagent.repository

import android.net.Uri
import com.example.nightagent.firebase.FirebaseConfig
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EvidenceRepository {
    
    suspend fun uploadFile(
    fileUri: Uri,
    userId: String,
    sosId: String
): Result<String> = withContext(Dispatchers.IO) {

    try {
        val timestamp = System.currentTimeMillis()

        val storageRef = FirebaseConfig.storage.reference
            .child("evidence/$userId/$timestamp.mp4")

        val snapshot = storageRef.putFile(fileUri).await()
        val downloadUrl = snapshot.storage.downloadUrl.await().toString()

        // 🔥 SAVE LINK IN FIRESTORE
        FirebaseConfig.firestore
            .collection("sos_logs")
            .document(sosId)
            .update("evidenceUrl", downloadUrl)
            .await()

        Result.success(downloadUrl)

    } catch (e: Exception) {
        Result.failure(e)
    }
}
}
