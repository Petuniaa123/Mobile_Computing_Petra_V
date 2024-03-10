package com.example.hw4.ui.theme

import android.app.Activity
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import com.example.hw4.R

class MySensorActivity : Activity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var gyro : Sensor? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        gyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        if (gyro == null) {
            Log.e("MySensorActivity", "Device does not have gyro.")
            // Voit tässä ilmoittaa käyttäjälle, että laite ei tue valosensoria tai jotain vastaavaa
        }
        //setContentView(R.layout.main_activity)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        event?.let { event ->
            val gx = event.values[0]
            val gy = event.values[1]
            val gz = event.values[2]

            if (Math.abs(gx) > 0.5 || Math.abs(gy) > 0.5 || Math.abs(gz) > 0.5) {
                Log.d("MySensorActivity", "Device has been moved.")
                Notifications().sendNotification(this)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        gyro?.also { gyroscope ->
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}