package com.example.nightagent.sos

import android.content.Context
import android.media.MediaRecorder
import android.os.Environment
import android.widget.Toast
import java.io.File

object EvidenceRecorder {

    private var recorder: MediaRecorder? = null
    private var file: File? = null

    fun startRecording(context: Context) {

        try {

            file = File(
                context.getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "evidence_recording.3gp"
            )

            recorder = MediaRecorder().apply {

                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                setOutputFile(file!!.absolutePath)

                prepare()
                start()
            }

            Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {

            e.printStackTrace()
            Toast.makeText(context, "Recording failed: ${e.message}", Toast.LENGTH_LONG).show()

        }
    }

    fun stopRecording(context: Context) {

        try {

            recorder?.apply {
                stop()
                release()
            }

            recorder = null

            Toast.makeText(context, "Recording saved", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {

            e.printStackTrace()
            Toast.makeText(context, "Stop recording failed: ${e.message}", Toast.LENGTH_LONG).show()

        }
    }
}