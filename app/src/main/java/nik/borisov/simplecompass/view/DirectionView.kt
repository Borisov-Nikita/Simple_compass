package nik.borisov.simplecompass.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class DirectionView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var middleX by Delegates.notNull<Int>()
    private var middleY by Delegates.notNull<Int>()

    private var direction by Delegates.notNull<String>()
    private var degree by Delegates.notNull<String>()

    private val directionRect = Rect(0, 0, 0, 0)
    private val degreeRect = Rect(0, 0, 0, 0)

    private var directionX by Delegates.notNull<Float>()
    private var directionY by Delegates.notNull<Float>()
    private var degreeX by Delegates.notNull<Float>()
    private var degreeY by Delegates.notNull<Float>()

    private var paddingBetween by Delegates.notNull<Float>()
    private var textSize by Delegates.notNull<Float>()

    private lateinit var paint: Paint

    init {
        initProperties()
        setupDefaultDirectionAttrs()
        initPaints()
    }

    fun setupDirectionAttrs(paddingBetween: Float, textSize: Float) {
        this.paddingBetween = paddingBetween
        this.textSize = textSize
        updatePaints()
    }

    fun onDirectionChanged(direction: String, degree: String) {
        this.direction = direction
        this.degree = degree
        measureTextsSize()
    }

    private fun initProperties() {

        //TODO string res
        direction = "N"
        degree = "360°"
    }

    private fun setupDefaultDirectionAttrs() {
        paddingBetween = DEFAULT_PADDING_BETWEEN
        textSize = DEFAULT_TEXT_SIZE
    }

    private fun initPaints() {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = this@DirectionView.textSize
            style = Paint.Style.STROKE
        }
    }

    private fun updatePaints() {
        paint.textSize = this@DirectionView.textSize
    }

    private fun setupAlignPoints(width: Int, height: Int) {
        middleX = width / 2
        middleY = height / 8
        measureTextsSize()
    }

    private fun measureTextsSize() {
        paint.getTextBounds(direction, 0, direction.length, directionRect)
        paint.getTextBounds(degree, 0, degree.length, degreeRect)
        updateTextPosition()
    }

    private fun updateTextPosition() {
        directionX = middleX - directionRect.width() - paddingBetween / 2
        directionY = (middleY + directionRect.height() / 2).toFloat()
        degreeX = middleX + paddingBetween / 2
        degreeY = (middleY + degreeRect.height() / 2).toFloat()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupAlignPoints(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawText(direction, directionX, directionY, paint)
        canvas?.drawText(degree, degreeX, degreeY, paint)
    }

    companion object {

        const val DEFAULT_PADDING_BETWEEN = 50f
        const val DEFAULT_TEXT_SIZE = 80f
    }
}