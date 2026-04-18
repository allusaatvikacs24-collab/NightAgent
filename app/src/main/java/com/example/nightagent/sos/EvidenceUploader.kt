package com.example.nightagent.sos

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.io.File

object EvidenceUploader {

    fun uploadRecording(context: Context) {

        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
            "evidence_recording.3gp"
        )

        if (!file.exists()) {
            Toast.makeText(context, "Recording file not found", Toast.LENGTH_LONG).show()
            return
        }

        val uri = Uri.fromFile(file)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("evidence/${file.name}")

        storageRef.putFile(uri)
            .addOnSuccessListener {

                storageRef.downloadUrl.addOnSuccessListener { url ->

                    Toast.makeText(
                        context,
                        "Evidence uploaded: $url",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }
            .addOnFailureListener { e ->



            }
    }
}