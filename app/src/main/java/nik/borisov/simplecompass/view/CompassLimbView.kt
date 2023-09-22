package nik.borisov.simplecompass.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import nik.borisov.simplecompass.R
import kotlin.properties.Delegates

class CompassLimbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var rotatePointX by Delegates.notNull<Float>()
    private var rotatePointY by Delegates.notNull<Float>()
    private var limbRadius by Delegates.notNull<Int>()

    private var azimuth by Delegates.notNull<Float>()

    private val compassLimb = ContextCompat.getDrawable(context, R.drawable.compass_limb_128)

    //TODO change color

    init {
        setupDefaultLimbAttrs()
    }

    fun onAzimuthChanged(azimuth: Float) {
        this.azimuth = azimuth
        invalidate()
    }

    private fun setupDefaultLimbAttrs() {
        azimuth = 0f
    }

    private fun setupLimbPosition(width: Int, height: Int) {
        val middleX = width / 2
        val middleY = height / 2
        rotatePointX = middleX.toFloat()
        rotatePointY = middleY.toFloat()
        limbRadius = width / 2

        compassLimb?.setBounds(
            middleX - limbRadius,
            middleY - limbRadius,
            middleX + limbRadius,
            middleY + limbRadius
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupLimbPosition(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.rotate(azimuth, rotatePointX, rotatePointY)
            compassLimb?.draw(canvas)
        }
    }
}