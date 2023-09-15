package nik.borisov.simplecompass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SensorsChecker(
    private val context: Context
) {

    private val sensorManager by lazy {
        context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    }

    private val sensors by lazy {
        sensorManager.getSensorList(Sensor.TYPE_ALL)
    }

    fun loggingAllSensors(tag: String) {
        if (sensors.isEmpty()) {
            Log.d(tag, "No sensors detected.")
            return
        }
        sensors.forEach { sensor ->
            val sensorInfo = "Sensor: name -> ${sensor.name}, type -> ${sensor.type}"
            Log.d(tag, sensorInfo)
        }
    }
}