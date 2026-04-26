package com.example.nightagent.sos

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
class VoiceSOSManager(
    private val context: Context,
    private val onKeywordDetected: () -> Unit
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var destroyed = false
    private var isEnabled = false   // 🔥 NEW: tracks toggle state
    private val handler = Handler(Looper.getMainLooper())

    private var lastTriggerTime = 0L
    private val COOLDOWN_MS = 5000L

    private val KEYWORDS = setOf(
        "help", "help me", "danger", "save me", "sos", "emergency", "call police", "save"
    )

    // 🔥 Call this when the user toggles ON
    fun enable() {
        if (isEnabled) return
        isEnabled = true
        destroyed = false
        Log.d("VoiceSOS", "Voice SOS ENABLED")
        startListening()
    }

    // 🔥 Call this when the user toggles OFF
    fun disable() {
        if (!isEnabled) return
        isEnabled = false
        Log.d("VoiceSOS", "Voice SOS DISABLED")
        stopListening()
        handler.removeCallbacksAndMessages(null)
    }

    fun startListening() {
        // 🔥 Guard: do nothing if not enabled by the toggle
        if (!isEnabled) {
            Log.d("VoiceSOS", "startListening blocked — toggle is OFF")
            return
        }

        Log.d("VOICE_STEP1", "startListening CALLED")
        if (isListening) {
            Log.d("VoiceSOS", "Already listening, skipping startListening()")
            return
        }

        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("VoiceSOS", "Microphone permission NOT granted")
            return
        }

        if (destroyed) {
            Log.w("VoiceSOS", "Manager destroyed, refusing to start")
            return
        }

        isListening = true

        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            }

            speechRecognizer?.setRecognitionListener(object : RecognitionListener {

                override fun onReadyForSpeech(params: Bundle?) {
                    Log.d("VoiceSOS", "Ready for speech")
                }

                override fun onResults(results: Bundle?) {
                    Log.d("VoiceSOS", "RESULT RECEIVED")
                    handleResults(results)
                    restartListening()
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    Log.d("VoiceSOS", "onPartialResults called")
                    handleResults(partialResults)
                }

                override fun onError(error: Int) {
                    val errorMsg = getErrorText(error)
                    Log.e("VoiceSOS", "SpeechRecognizer error: $errorMsg (code=$error)")
                    isListening = false
                    restartListening()   // restartListening also checks isEnabled
                }

                override fun onEndOfSpeech() {
                    Log.d("VoiceSOS", "End of speech")
                    isListening = false
                    restartListening()
                }

                override fun onBeginningOfSpeech() { Log.d("VoiceSOS", "Beginning of speech") }
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            speechRecognizer?.startListening(intent)
            Log.d("VoiceSOS", "startListening() invoked on SpeechRecognizer")

        } catch (e: Exception) {
            Log.e("VoiceSOS", "Failed to start listening: ${e.message}", e)
            isListening = false
            restartListening()
        }
    }

    private fun handleResults(bundle: Bundle?) {
        val matches = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        if (matches.isNullOrEmpty()) {
            Log.d("VoiceSOS", "No speech results")
            return
        }

        matches.forEach { text ->
            val lower = text.lowercase()
            Log.d("VoiceSOS", "Heard: \"$lower\"")

            val matchedKeyword = KEYWORDS.find { keyword ->
                when {
                    keyword.contains(" ") -> lower.contains(keyword)
                    else -> lower.split(" ", ",", "!", ".", "?").contains(keyword)
                }
            }

            if (matchedKeyword != null) {
                val now = System.currentTimeMillis()
                if (now - lastTriggerTime > COOLDOWN_MS) {
                    lastTriggerTime = now
                    Log.d("VoiceSOS", "🚨 KEYWORD MATCHED: \"$matchedKeyword\" — VOICE SOS TRIGGERED")
                    Toast.makeText(context, "Voice SOS detected: \"$matchedKeyword\"", Toast.LENGTH_SHORT).show()
                    onKeywordDetected()
                } else {
                    Log.d("VoiceSOS", "Keyword matched but within cooldown")
                }
            }
        }
    }

    private fun restartListening() {
        handler.removeCallbacksAndMessages(null)

        // 🔥 Do NOT restart if disabled or destroyed
        if (!isEnabled || destroyed) {
            Log.d("VoiceSOS", "Skipping restart — enabled=$isEnabled, destroyed=$destroyed")
            return
        }

        handler.postDelayed({
            stopListening()
            startListening()
        }, 500)
    }

    fun stopListening() {
        isListening = false
        try { speechRecognizer?.stopListening() } catch (e: Exception) { Log.e("VoiceSOS", "stopListening error: ${e.message}") }
        try { speechRecognizer?.cancel() } catch (e: Exception) { Log.e("VoiceSOS", "cancel error: ${e.message}") }
        try { speechRecognizer?.destroy() } catch (e: Exception) { Log.e("VoiceSOS", "destroy error: ${e.message}") }
        speechRecognizer = null
    }

    fun destroy() {
        destroyed = true
        isEnabled = false
        stopListening()
        handler.removeCallbacksAndMessages(null)
        Log.d("VoiceSOS", "VoiceSOSManager destroyed")
    }

    private fun getErrorText(errorCode: Int): String {
        return when (errorCode) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
    }
}