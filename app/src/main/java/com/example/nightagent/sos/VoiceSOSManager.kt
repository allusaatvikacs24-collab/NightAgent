package com.example.nightagent.sos

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class VoiceSOSManager(
    private val context: Context,
    private val onKeywordDetected: () -> Unit
) {

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening() {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }

        speechRecognizer.setRecognitionListener(object : android.speech.RecognitionListener {

            override fun onResults(results: android.os.Bundle?) {

                val matches =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

                matches?.forEach {

                    val text = it.lowercase()

                    if (text.contains("help") || text.contains("emergency")) {
                        onKeywordDetected()
                    }
                }

                startListening()
            }

            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) { startListening() }
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        })

        speechRecognizer.startListening(intent)
    }
}