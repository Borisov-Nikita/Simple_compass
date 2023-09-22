package nik.borisov.simplecompass.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import nik.borisov.simplecompass.R
import kotlin.math.sqrt
import kotlin.properties.Delegates

class BubbleLevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var middleX by Delegates.notNull<Int>()
    private var middleY by Delegates.notNull<Int>()

    private var levelRadius by Delegates.notNull<Int>()
    private var markerRadius by Delegates.notNull<Int>()
    private var bubbleRadius by Delegates.notNull<Int>()
    private var bubblePositionMaxRadius by Delegates.notNull<Float>()
    private var bubblePositionRadius by Delegates.notNull<Float>()

    private var coordinateX by Delegates.notNull<Float>()
    private var coordinateY by Delegates.notNull<Float>()
    private var biasX by Delegates.notNull<Float>()
    private var biasY by Delegates.notNull<Float>()

    private var levelBorder = ContextCompat.getDrawable(context, R.drawable.level_border_64)
    private var levelMarker = ContextCompat.getDrawable(context, R.drawable.level_marker_32)
    private var bubble = ContextCompat.getDrawable(context, R.drawable.level_bubble_32)

    private fun setupLevelSizeAndPosition(width: Int, height: Int) {
        middleX = width / 2
        middleY = height / 2

        levelRadius = (width * LEVEL_FIELD_DIAMETER_RATIO / 2).toInt()
        markerRadius = (levelRadius * LEVEL_MARKER_DIAMETER_RATIO).toInt()
        bubbleRadius = (markerRadius * BUBBLE_DIAMETER_RATIO).toInt()

        bubblePositionMaxRadius = (levelRadius - bubbleRadius).toFloat()

        levelBorder?.setBounds(
            middleX - levelRadius,
            middleY - levelRadius,
            middleX + levelRadius,
            middleY + levelRadius
        )
        levelMarker?.setBounds(
            middleX - markerRadius,
            middleY - markerRadius,
            middleX + markerRadius,
            middleY + markerRadius
        )

        coordinateX = middleX.toFloat()
        coordinateY = middleY.toFloat()
    }

    fun onCoordinateBiasesChanged(biasX: Float, biasY: Float) {
        this.biasX = biasX
        this.biasY = biasY
        updateBubblePosition()
    }

    private fun updateBubblePosition() {
        coordinateX = middleX + biasX * levelRadius
        coordinateY = middleY + biasY * levelRadius

        bubblePositionRadius =
            sqrt((middleX - coordinateX) * (middleX - coordinateX) + (middleY - coordinateY) * (middleY - coordinateY))

        if (bubblePositionRadius > bubblePositionMaxRadius) {
            coordinateX =
                (coordinateX - middleX) * bubblePositionMaxRadius / bubblePositionRadius + middleX
            coordinateY =
                (coordinateY - middleY) * bubblePositionMaxRadius / bubblePositionRadius + middleY
        }

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupLevelSizeAndPosition(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            bubble?.setBounds(
                (coordinateX - bubbleRadius).toInt(),
                (coordinateY - bubbleRadius).toInt(),
                (coordinateX + bubbleRadius).toInt(),
                (coordinateY + bubbleRadius).toInt()
            )
            bubble?.draw(canvas)
            levelBorder?.draw(canvas)
            levelMarker?.draw(canvas)
        }
    }

    companion object {

        private const val LEVEL_FIELD_DIAMETER_RATIO = 0.5
        private const val LEVEL_MARKER_DIAMETER_RATIO = 0.5
        private const val BUBBLE_DIAMETER_RATIO = 0.8
    }
}