package com.example.nightagent.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage

object FirebaseConfig {

    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val messaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance()
    }
}
