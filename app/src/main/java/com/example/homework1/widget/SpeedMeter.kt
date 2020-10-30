package com.example.homework1.widget

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.homework1.R
import com.example.homework1.utils.toRadian
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

interface SpeedChangeListener {
    fun onSpeedChanged(newSpeed: Int)
}

class SpeedMeter @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes), SpeedChangeListener {

    private var limitSpeed: Int
    private var currentSpeed: Int = 0

    private var size = 650

    private var outsideRadius: Float = (size / 2).toFloat()
    private var insideRadius: Float = outsideRadius * 0.8f
    private var arrowLength = outsideRadius * 0.85f

    private var centerX: Float = (size / 2).toFloat()
    private var centerY: Float = (size / 2).toFloat()
    private var numberOfMainTicks = DEFAULT_MAX_SPEED / DEFAULT_NUMBER_SEGMENT_STEP

    private var maxSpeed = DEFAULT_MAX_SPEED

    private val arrowLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 10f
        color = DEFAULT_ARROW_COLOR
    }

    private val ticksLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 5f
        color = Color.BLACK
    }

    private val labelsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.BLACK
        textSize = DEFAULT_TEXT_SIZE
    }

    private val speedLimitReached = ValueAnimator().apply {
        setIntValues(DEFAULT_ARROW_COLOR, DEFAULT_ALARM_ARROW_COLOR)
        setEvaluator(ArgbEvaluator())
        addUpdateListener {
            (this.animatedValue as? Int)?.let {
                arrowLinePaint.color = it
                invalidate()
            }
        }
        duration = 200
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.SpeedMeter,
            defStyleAttr,
            defStyleRes
        )

        try {
            maxSpeed = typedArray.getInt(R.styleable.SpeedMeter_maxSpeed, DEFAULT_MAX_SPEED)

            labelsPaint.textSize =
                typedArray.getDimension(R.styleable.SpeedMeter_labelTextSize, DEFAULT_TEXT_SIZE)

            limitSpeed =
                typedArray.getInt(R.styleable.SpeedMeter_limitSpeed, DEFAULT_SPEED_LIMIT)

        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureDimension(size, widthMeasureSpec)
        val height = measureDimension(size, heightMeasureSpec)

        size = min(width, height)

        outsideRadius = (size / 2).toFloat()
        insideRadius = outsideRadius * 0.8f
        arrowLength = outsideRadius * 0.85f

        centerX = (size / 2).toFloat()
        centerY = (size / 2).toFloat()

        numberOfMainTicks = maxSpeed / DEFAULT_NUMBER_SEGMENT_STEP

        setMeasuredDimension(width, height)
    }

    private fun measureDimension(minSize: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.AT_MOST -> minSize.coerceAtMost(specSize)
            else -> minSize
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            drawTicks(it)
            drawLabels(it)
            drawArrow(it)
        }
    }

    private fun drawTicks(canvas: Canvas) {

        val stepAngle = 180 / numberOfMainTicks

        for (i in 0 until numberOfMainTicks + 1) {
            val angle = (stepAngle * i).toFloat().toRadian()

            val lineBeginX = (centerX - insideRadius * cos(angle))
            val lineBeginY = (centerY - insideRadius * sin(angle))

            val lineEndX = (centerX - outsideRadius * cos(angle))
            val lineEndY = (centerY - outsideRadius * sin(angle))

            canvas.drawLine(lineBeginX, lineBeginY, lineEndX, lineEndY, ticksLinePaint)
        }
    }

    private fun drawArrow(canvas: Canvas) {

        val arrowAngle =
            diapasonMapper(currentSpeed.toFloat(), 0f, maxSpeed.toFloat(), 0f, 180f).toRadian()

        val lineEndX = (centerX - arrowLength * cos(arrowAngle))
        val lineEndY = (centerY - arrowLength * sin(arrowAngle))

        canvas.drawLine(centerX, centerY, lineEndX, lineEndY, arrowLinePaint)
    }

    private fun drawLabels(canvas: Canvas) {
        labelsPaint.textAlign = Paint.Align.LEFT
        canvas.drawText("0", 0f, centerY + labelsPaint.textSize, labelsPaint)

        labelsPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            maxSpeed.toString(),
            size.toFloat(),
            centerY + labelsPaint.textSize,
            labelsPaint
        )
    }

    private fun diapasonMapper(
        value: Float,
        smin: Float,
        smax: Float,
        dmin: Float,
        dmax: Float
    ): Float =
        ((value - smin) / (smax - smin)) * (dmax - dmin) + dmin

    fun getSpeed() = currentSpeed

    override fun onSpeedChanged(newSpeed: Int) {

        currentSpeed = newSpeed.coerceAtMost(maxSpeed).coerceAtLeast(0)

        if (currentSpeed > limitSpeed)
            speedLimitReached.start()
        else arrowLinePaint.color = DEFAULT_ARROW_COLOR

        invalidate()
    }

    companion object {
        private const val DEFAULT_MAX_SPEED = 240
        private const val DEFAULT_SPEED_LIMIT = 100
        private const val DEFAULT_NUMBER_SEGMENT_STEP = 20
        private const val DEFAULT_TEXT_SIZE = 60f

        private const val DEFAULT_ARROW_COLOR = Color.GREEN
        private const val DEFAULT_ALARM_ARROW_COLOR = Color.RED
    }
}
