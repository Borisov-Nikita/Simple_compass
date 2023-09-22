package nik.borisov.simplecompass.orientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity

class OrientationProvider(
    private val context: Context,
    private val onOrientationChanged: (values: OrientationValues) -> Unit
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

    private val supported by lazy {
        sensorAccelerometer != null && sensorMagneticField != null
    }

    private val gravityValues = FloatArray(3)
    private val geoMagneticValues = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val inclinationMatrix = FloatArray(9)
    private val orientation = FloatArray(3)

    private var pitch = 0f
    private var roll = 0f
    private var azimuth = 0f


    fun start(): Boolean {
        if (!supported) return false
        registerSensorListener(sensorAccelerometer)
        registerSensorListener(sensorMagneticField)
        return true
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    private fun registerSensorListener(sensor: Sensor?) {
        sensorManager.registerListener(
            this,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun updateSensorValues(source: FloatArray, newValues: FloatArray) {
        for (index in source.indices) {
            source[index] = newValues[index]
        }
    }

    private fun updateOrientation() {
        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            inclinationMatrix,
            gravityValues,
            geoMagneticValues
        )
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientation)

            pitch = orientation[1]
            roll = -orientation[2]
            azimuth = orientation[0]

            onOrientationChanged.invoke(OrientationValues(pitch, roll, azimuth))
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        synchronized(this) {
            when (event?.sensor?.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    updateSensorValues(gravityValues, event.values)
                }

                Sensor.TYPE_MAGNETIC_FIELD -> {
                    updateSensorValues(geoMagneticValues, event.values)
                }
            }
            updateOrientation()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}