package nik.borisov.simplecompass.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import nik.borisov.simplecompass.R
import nik.borisov.simplecompass.databinding.CompassWithLevelLayoutBinding

class CompassWithLevelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding by lazy {
        CompassWithLevelLayoutBinding.bind(this)
    }

    init {
        LayoutInflater.from(context).apply {
            inflate(R.layout.compass_with_level_layout, this@CompassWithLevelView, true)
        }
        initAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        if (attrs == null) return
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CompassWithLevelView,
            defStyleAttr,
            defStyleRes
        )
        initPointerView(typedArray)
        initDirectionView(typedArray)
        typedArray.recycle()
    }

    private fun initPointerView(typedArray: TypedArray) {
        val sizeRatio = typedArray.getFloat(
            R.styleable.CompassWithLevelView_pointer_size_ratio,
            CompassPointerView.POINTER_SIZE_DEFAULT_RATIO
        )
        val pointerAlign = typedArray.getInteger(
            R.styleable.CompassWithLevelView_pointer_alignment,
            CompassPointerView.POINTER_DEFAULT_ALIGN.value
        )
        binding.compassPointerView.setupPointerAttrs(sizeRatio, pointerAlign)
    }

    private fun initDirectionView(typedArray: TypedArray) {
        val paddingBetween = typedArray.getDimension(
            R.styleable.CompassWithLevelView_direction_padding_between,
            DirectionView.DEFAULT_PADDING_BETWEEN
        )
        val textSize = typedArray.getDimension(
            R.styleable.CompassWithLevelView_android_textSize,
            DirectionView.DEFAULT_TEXT_SIZE
        )
        binding.directionView.setupDirectionAttrs(paddingBetween, textSize)
    }

    fun onOrientationChanged(
        biasX: Float,
        biasY: Float,
        azimuth: Float,
        direction: String,
        degree: String
    ) {
        binding.compassLimbView.onAzimuthChanged(azimuth)
        binding.bubbleLevelView.onCoordinateBiasesChanged(biasX, biasY)
        binding.directionView.onDirectionChanged(direction, degree)
    }
}