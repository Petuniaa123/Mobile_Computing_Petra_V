package com.example.project.ui.theme

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlin.math.abs

class SensorTest(private val context: Context) : SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gyro: Sensor? = null

    fun init() {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        if (gyro == null) {
            // Device does not have gyro
            Log.d("SensorTest", "Device does not have gyro")
        }
    }

    fun startSensor() {
        gyro?.also { gyroscope ->
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // If later needed
    fun stopSensor() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No need to use this
    }

    override fun onSensorChanged(event: SensorEvent) {
        val gx = event.values[0]
        val gy = event.values[1]
        val gz = event.values[2]

        if (abs(gx) > 0.5 || abs(gy) > 0.5 || abs(gz) > 0.5) {
            val data = workDataOf("gx" to gx, "gy" to gy, "gz" to gz)
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInputData(data)
                .build()
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(workRequest)
        }
    }
}