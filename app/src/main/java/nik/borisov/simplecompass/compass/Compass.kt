package nik.borisov.simplecompass.compass

import kotlin.math.abs
import kotlin.math.log10
import kotlin.properties.Delegates

class Compass {

    private var currentAzimuth by Delegates.notNull<Float>()

    private var currentDirection by Delegates.notNull<Int>()
    private var currentDegree by Delegates.notNull<Int>()

    init {
        initProperties()
    }

    fun getFilteredAzimuth(azimuthInRadian: Float): CompassValues {
        val azimuthInDegrees = Math.toDegrees(azimuthInRadian.toDouble()).toFloat()
        filterAzimuth(azimuthInDegrees)
        updateDirection()
        return CompassValues(-currentAzimuth, currentDirection, "$currentDegree°")
    }

    private fun initProperties() {
        currentAzimuth = DEFAULT_AZIMUTH
    }

    private fun filterAzimuth(newAzimuth: Float) {
        var azimuthFrom = currentAzimuth
        var azimuthTo = newAzimuth

        val diff = abs(azimuthTo - azimuthFrom)

        if (diff > HALF_CIRCLE) {
            val minValue = azimuthFrom.coerceAtMost(azimuthTo)
            if (minValue == azimuthFrom) {
                azimuthFrom += FULL_CIRCLE
            } else {
                azimuthTo += FULL_CIRCLE
            }
        }

        val rotationAngle = azimuthTo - azimuthFrom

        currentAzimuth = currentAzimuth.calculateValueWithCorrection(
            rotationAngle
        )
    }

    private fun Float.calculateValueWithCorrection(rotationAngle: Float): Float {
        val correction = 1 - log10(abs(rotationAngle) + 1) / 10
        return (this * correction + (1 - correction) * (this + rotationAngle) % 360) % 360
    }

    private fun updateDirection() {
        currentDegree = (360 + currentAzimuth.toInt()) % 360
        currentDirection = when (currentDegree) {
            in 23 until 68 -> CompassDirection.NORTHEAST.direction
            in 69 until 113 -> CompassDirection.EAST.direction
            in 113 until 158 -> CompassDirection.SOUTHEAST.direction
            in 158 until 203 -> CompassDirection.SOUTH.direction
            in 203 until 248 -> CompassDirection.SOUTHWEST.direction
            in 248 until 293 -> CompassDirection.WEST.direction
            in 293 until 338 -> CompassDirection.NORTHWEST.direction
            else -> CompassDirection.NORTH.direction
        }
    }

    companion object {

        private const val DEFAULT_AZIMUTH = 0f
        private const val FULL_CIRCLE = 360f
        private const val HALF_CIRCLE = 180f
    }
}