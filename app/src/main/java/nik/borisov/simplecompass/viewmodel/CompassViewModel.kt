package nik.borisov.simplecompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import nik.borisov.simplecompass.compass.Compass
import nik.borisov.simplecompass.level.BubbleLevel
import nik.borisov.simplecompass.orientation.OrientationProvider
import nik.borisov.simplecompass.orientation.OrientationValues

class CompassViewModel(application: Application) : AndroidViewModel(application) {

    private val compass = Compass()
    private val bubbleLevel = BubbleLevel()
    private val orientationProvider = OrientationProvider(application) { orientationValues ->
        refreshOrientation(orientationValues)
    }

    private val _orientation = MutableLiveData<CompassAndLevelValues>()
    val orientation: LiveData<CompassAndLevelValues>
        get() = _orientation

    fun start(): Boolean {
        return orientationProvider.start()
    }

    fun stop() {
        orientationProvider.stop()
    }

    private fun refreshOrientation(orientation: OrientationValues) {
        val compassValues = compass.getFilteredAzimuth(orientation.azimuth)
        val bubbleCoordinatesBias =
            bubbleLevel.getCoordinateBias(orientation.pitch, orientation.roll)
        _orientation.value = CompassAndLevelValues(
            bubbleCoordinatesBias.biasX,
            bubbleCoordinatesBias.biasY,
            compassValues.azimuth,
            compassValues.direction,
            compassValues.degree
        )
    }
}