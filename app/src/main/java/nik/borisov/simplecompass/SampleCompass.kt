package nik.borisov.simplecompass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity

class SampleCompass(
    private val context: Context,
    private val onAzimuthChanged: (Float) -> Unit
) : SensorEventListener {

    private val sensorManager by lazy {
        context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
    }
    private val sensorAccelerometer by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }
    private val sensorMagneticField by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    private val gravityValues = FloatArray(3)
    private val geoMagneticValues = FloatArray(3)
    private val inclinationMatrix = FloatArray(9)
    private val rotationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var azimuth = 0f

    fun startCompass() {
        registerSensorListener(sensorAccelerometer)
        registerSensorListener(sensorMagneticField)
    }

    fun stopCompass() {
        sensorManager.unregisterListener(this)
    }

    private fun registerSensorListener(sensor: Sensor?) {
        if (sensor != null) {
            sensorManager.registerListener(
                this,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    private fun updateValues(source: FloatArray, newValues: FloatArray) {
        for (index in source.indices) {
            source[index] = newValues[index]
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        synchronized(this) {
            when (event?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    updateValues(gravityValues, event.values)
                }

                Sensor.TYPE_MAGNETIC_FIELD -> {
                    updateValues(geoMagneticValues, event.values)
                }
            }

            val success = SensorManager.getRotationMatrix(
                rotationMatrix,
                inclinationMatrix,
                gravityValues,
                geoMagneticValues
            )
            if (success) {
                SensorManager.getOrientation(rotationMatrix, orientation)
                azimuth = (Math.toDegrees(orientation[0].toDouble()).toFloat() + 360) % 360
                onAzimuthChanged.invoke(azimuth)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}