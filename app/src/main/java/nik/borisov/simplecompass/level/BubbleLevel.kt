package nik.borisov.simplecompass.level

import kotlin.math.sin
import kotlin.properties.Delegates

class BubbleLevel {

    private var currentBiasX by Delegates.notNull<Float>()
    private var currentBiasY by Delegates.notNull<Float>()

    private var currentAngleX by Delegates.notNull<Float>()
    private var currentAngleY by Delegates.notNull<Float>()
    private var speedX by Delegates.notNull<Float>()
    private var speedY by Delegates.notNull<Float>()

    private var currentTime by Delegates.notNull<Long>()
    private var lastTime by Delegates.notNull<Long>()
    private var timeDiff by Delegates.notNull<Float>()

    init {
        initProperties()
    }

    fun getCoordinateBias(pitch: Float, roll: Float): BubbleCoordinateBiases {
        updateAngles(pitch, roll)
        updateBias()
        return BubbleCoordinateBiases(currentBiasX, currentBiasY)
    }

    private fun initProperties() {
        currentBiasX = 0f
        currentBiasY = 0f
        currentAngleX = 0f
        currentAngleY = 0f
        lastTime = 0L
    }

    private fun updateAngles(pitch: Float, roll: Float) {
        currentAngleX = (currentAngleX * 0.7f + sin(roll) / MAX_SINUS * 0.3f).toFloat()
        currentAngleY = (currentAngleY * 0.7f + sin(pitch) / MAX_SINUS * 0.3f).toFloat()

        if (currentAngleX > 1) {
            currentAngleX = 1f
        } else if (currentAngleX < -1) {
            currentAngleX = -1f
        }
        if (currentAngleY > 1) {
            currentAngleY = 1f
        } else if (currentAngleY < -1) {
            currentAngleY = -1f
        }
    }

    private fun updateBias() {
        currentTime = System.currentTimeMillis()
        if (lastTime > 0) {
            timeDiff = (currentTime - lastTime) / 1000f

            speedX = currentAngleX - currentBiasX
            speedY = currentAngleY - currentBiasY

            currentBiasX += speedX * timeDiff
            currentBiasY += speedY * timeDiff
        }
        lastTime = currentTime
    }

    companion object {

        private val MAX_SINUS = sin(Math.PI / 4)
    }
}