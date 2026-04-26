package com.example.nightagent.sos

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
    context: Context,
    private val onShake: () -> Unit
) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var lastShakeTime = 0L
    private var shakeCount = 0

    // 🔥 FILTER VARIABLES
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    fun start() {
        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        // 🔥 REMOVE GRAVITY EFFECT (difference)
        val deltaX = x - lastX
        val deltaY = y - lastY
        val deltaZ = z - lastZ

        lastX = x
        lastY = y
        lastZ = z

        val acceleration = sqrt(
            (deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()
        )

        val now = System.currentTimeMillis()

        // 🔥 STRONG SHAKE ONLY
        if (acceleration > 18) {

            // Count shakes within short time
            if (now - lastShakeTime < 1000) {
                shakeCount++
            } else {
                shakeCount = 1
            }

            lastShakeTime = now

            // 🔥 REQUIRE MULTIPLE SHAKES
            if (shakeCount >= 2) {

                shakeCount = 0

                // Cooldown to prevent spam
                if (now - lastShakeTime > 2000) return

                if (SafetySettings.shakeDetection.value) {
                    onShake()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}