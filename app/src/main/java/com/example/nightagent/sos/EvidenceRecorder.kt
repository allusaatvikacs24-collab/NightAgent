package com.example.nightagent.sos

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EvidenceRecorder {

    private var mediaRecorder: MediaRecorder? = null
    private var lastRecordedFile: File? = null
    var isRecording = false // Made public for UI

    fun startRecording(context: Context, lifecycleOwner: LifecycleOwner) {
        if (isRecording) return

        Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()

        val moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        val nightAgentDir = File(moviesDir, "NightAgent")
        if (!nightAgentDir.exists()) {
            nightAgentDir.mkdirs()
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(nightAgentDir, "evidence_$timestamp.mp4")
        lastRecordedFile = file

        mediaRecorder = MediaRecorder().apply {
            val profile = android.media.CamcorderProfile.get(android.media.CamcorderProfile.QUALITY_480P)
            setProfile(profile)
            setOutputFile(file.absolutePath)
            setOrientationHint(90)
            setVideoFrameRate(30)

            try {
                prepare()
                start()
                isRecording = true
            } catch (e: Exception) {
                Toast.makeText(context, "Recording failed: ${e.message}", Toast.LENGTH_LONG).show()
                release()
            }
        }
    }

    fun stopRecording(context: Context) {
        if (!isRecording) return

        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false

            lastRecordedFile?.let { file ->
                Toast.makeText(context, "Recording stopped and saved: ${file.name}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Stop recording failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLastRecordedFile(): File? = lastRecordedFile
}
