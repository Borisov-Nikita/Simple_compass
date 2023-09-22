package nik.borisov.simplecompass.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import nik.borisov.simplecompass.R
import kotlin.properties.Delegates

enum class PointerAlignment(val value: Int) {
    TOP(-1),
    CENTER(0),
    BOTTOM(1)
}

class CompassPointerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var pointerSizeRatio by Delegates.notNull<Float>()
    private var pointerAlign by Delegates.notNull<PointerAlignment>()

    private val compassPointer = ContextCompat.getDrawable(context, R.drawable.compass_pointer_32)

    //TODO change color

    init {
        setupDefaultPointerAttrs()
    }

    fun setupPointerAttrs(pointerSizeRatio: Float, pointerAlign: Int) {
        this.pointerSizeRatio = checkPointerSizeRatio(pointerSizeRatio)
        this.pointerAlign = when (pointerAlign) {
            PointerAlignment.TOP.value -> PointerAlignment.TOP
            PointerAlignment.CENTER.value -> PointerAlignment.CENTER
            PointerAlignment.BOTTOM.value -> PointerAlignment.BOTTOM
            else -> POINTER_DEFAULT_ALIGN
        }
        updatePointerPosition()
    }

    private fun setupDefaultPointerAttrs() {
        pointerSizeRatio = POINTER_SIZE_DEFAULT_RATIO
        pointerAlign = POINTER_DEFAULT_ALIGN
    }

    private fun checkPointerSizeRatio(ratio: Float): Float {
        if (ratio < POINTER_SIZE_MIN_RATIO) return POINTER_SIZE_MIN_RATIO
        if (ratio > POINTER_SIZE_MAX_RATIO) return POINTER_SIZE_MAX_RATIO
        return ratio
    }

    private fun getPointerWidthWithRatio(): Int {
        return (width * pointerSizeRatio * POINTER_SIZE_BASIC_RATIO).toInt()
    }

    private fun updatePointerPosition() {
        val pointerWidth = getPointerWidthWithRatio()
        val pointerHeight = pointerWidth * 2
        val limbRadius = width / 2
        val middleX = width / 2
        val middleY = height / 2

        compassPointer?.setBounds(
            middleX - pointerWidth / 2,
            middleY - limbRadius,
            middleX + pointerWidth / 2,
            middleY - limbRadius + pointerHeight
        )

        when (pointerAlign) {
            PointerAlignment.TOP -> {
                compassPointer?.setBounds(
                    middleX - pointerWidth / 2,
                    middleY - limbRadius,
                    middleX + pointerWidth / 2,
                    middleY - limbRadius + pointerHeight
                )
            }

            PointerAlignment.CENTER -> {
                compassPointer?.setBounds(
                    middleX - pointerWidth / 2,
                    middleY - limbRadius - pointerHeight / 2,
                    middleX + pointerWidth / 2,
                    middleY - limbRadius + pointerHeight / 2
                )
            }

            PointerAlignment.BOTTOM -> {
                compassPointer?.setBounds(
                    middleX - pointerWidth / 2,
                    middleY - limbRadius - pointerHeight,
                    middleX + pointerWidth / 2,
                    middleY - limbRadius
                )
            }
        }
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updatePointerPosition()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null)
            compassPointer?.draw(canvas)
    }

    companion object {

        const val POINTER_SIZE_DEFAULT_RATIO = 0.5f
        val POINTER_DEFAULT_ALIGN = PointerAlignment.TOP

        private const val POINTER_SIZE_BASIC_RATIO = 0.1f
        private const val POINTER_SIZE_MIN_RATIO = 0.1f
        private const val POINTER_SIZE_MAX_RATIO = 1f
    }
}