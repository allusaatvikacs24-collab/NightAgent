package com.example.nightagent.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object AuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun loginAnonymous(): Result<String> = try {
        val result = auth.signInAnonymously().await()
        Result.success(result.user?.uid ?: "")
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun loginWithEmail(email: String, password: String): Result<String> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        Result.success(result.user?.uid ?: "")
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createUser(email: String, password: String): Result<String> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        Result.success(result.user?.uid ?: "")
    } catch (e: Exception) {
        Result.failure(e)
    }

    fun logout() {
        auth.signOut()
    }

    fun addAuthStateListener(listener: (String?) -> Unit) {
        auth.addAuthStateListener {
            listener(it.currentUser?.uid)
        }
    }

    // 🔥 SAVE FCM TOKEN
    fun saveFCMToken(token: String) {
        val userId = getCurrentUserId() ?: return

        FirebaseConfig.firestore
            .collection("users")
            .document(userId)
            .set(mapOf("fcmToken" to token), SetOptions.merge())
    }
}