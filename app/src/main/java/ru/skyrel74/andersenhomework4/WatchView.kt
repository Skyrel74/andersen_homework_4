package ru.skyrel74.andersenhomework4

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.min

class WatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_DAIL_COLOR = Color.BLACK
        private const val DEFAULT_HOUR_HAND_COLOR = Color.BLACK
        private const val DEFAULT_MINUTE_HAND_COLOR = Color.RED
        private const val DEFAULT_SECOND_HAND_COLOR = Color.BLUE
        private const val DEFAULT_DIAL_LENGTH = 45.0f
        private const val DEFAULT_DIAL_WIDTH = 25.0f
        private const val DEFAULT_HOUR_HAND_LENGTH = 45.0f
        private const val DEFAULT_HOUR_HAND_WIDTH = 25.0f
        private const val DEFAULT_MINUTE_HAND_LENGTH = 35.0f
        private const val DEFAULT_MINUTE_HAND_WIDTH = 25.0f
        private const val DEFAULT_SECOND_HAND_LENGTH = 25.0f
        private const val DEFAULT_SECOND_HAND_WIDTH = 25.0f
    }

    private val paint = Paint()

    private var dialColor = DEFAULT_DAIL_COLOR
    private var hourHandColor = DEFAULT_HOUR_HAND_COLOR
    private var minuteHandColor = DEFAULT_MINUTE_HAND_COLOR
    private var secondHandColor = DEFAULT_SECOND_HAND_COLOR

    private var dialWidth = DEFAULT_DIAL_WIDTH
    private var dialLength = DEFAULT_DIAL_LENGTH
    private var hourHandLength = DEFAULT_HOUR_HAND_LENGTH
    private var hourHandWidth = DEFAULT_HOUR_HAND_WIDTH
    private var minuteHandLength = DEFAULT_MINUTE_HAND_LENGTH
    private var minuteHandWidth = DEFAULT_MINUTE_HAND_WIDTH
    private var secondHandLength = DEFAULT_SECOND_HAND_LENGTH
    private var secondHandWidth = DEFAULT_SECOND_HAND_WIDTH
    private var radius = 0f

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.WatchView, 0, 0)

        dialColor =
            typedArray.getColor(R.styleable.WatchView_dialColor, DEFAULT_DAIL_COLOR)
        hourHandColor =
            typedArray.getColor(R.styleable.WatchView_hourHandColor, DEFAULT_HOUR_HAND_COLOR)
        minuteHandColor =
            typedArray.getColor(R.styleable.WatchView_minuteHandColor, DEFAULT_MINUTE_HAND_COLOR)
        secondHandColor =
            typedArray.getColor(R.styleable.WatchView_secondHandColor, DEFAULT_SECOND_HAND_COLOR)
        dialWidth =
            typedArray.getDimension(R.styleable.WatchView_dialWidth, DEFAULT_DIAL_WIDTH)
        dialLength =
            typedArray.getDimension(R.styleable.WatchView_dialLength, DEFAULT_DIAL_LENGTH)
        hourHandLength =
            typedArray.getDimension(R.styleable.WatchView_hourHandLength, DEFAULT_HOUR_HAND_LENGTH)
        hourHandWidth =
            typedArray.getDimension(R.styleable.WatchView_hourHandWidth, DEFAULT_HOUR_HAND_WIDTH)
        minuteHandLength =
            typedArray.getDimension(R.styleable.WatchView_minuteHandLength, DEFAULT_MINUTE_HAND_LENGTH)
        minuteHandWidth =
            typedArray.getDimension(R.styleable.WatchView_minuteHandWidth, DEFAULT_MINUTE_HAND_WIDTH)
        secondHandLength =
            typedArray.getDimension(R.styleable.WatchView_secondHandLength, DEFAULT_SECOND_HAND_LENGTH)
        secondHandWidth =
            typedArray.getDimension(R.styleable.WatchView_secondHandWidth, DEFAULT_SECOND_HAND_WIDTH)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val size = min(measuredWidth.toFloat(), measuredHeight.toFloat()).toInt()
        radius = size / 2f
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(radius, radius)
        drawDial(canvas)
        drawClockwise(canvas)
        invalidate()
    }

    private fun drawDial(canvas: Canvas) {
        paint.color = dialColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dialWidth
        canvas.drawCircle(0f, 0f, radius - dialWidth, paint)
        for (i in 1..12) {
            canvas.rotate(30f)
            canvas.drawLine(0f, -radius + dialWidth, 0f, -radius + dialLength + dialWidth, paint)
        }
    }

    private fun drawClockwise(canvas: Canvas) {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val hour = calendar[Calendar.HOUR]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        val millisecond = calendar[Calendar.MILLISECOND]

        val angleHour = (hour + minute / 60f) / 12 * 360
        val angleMinute = (minute + second / 60f) / 60 * 360
        val angleSecond = (second + millisecond / 1000f) / 60 * 360

        canvas.save()
        canvas.rotate(angleHour)
        val hourHand = RectF(
            -hourHandLength / 12,
            -radius * hourHandLength / 48 * 4/5,
            hourHandWidth / 12,
            radius * hourHandLength / 48 / 5
        )
        paint.color = hourHandColor
        paint.strokeWidth = hourHandWidth
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(hourHand, paint)
        canvas.restore()

        canvas.save()
        canvas.rotate(angleMinute)
        val minuteHand = RectF(
            -minuteHandWidth / 12,
            -radius * minuteHandLength / 48 * 4/5,
            minuteHandWidth / 12,
            radius * minuteHandLength / 48 / 5
        )
        paint.color = minuteHandColor
        paint.strokeWidth = minuteHandWidth
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(minuteHand, paint)
        canvas.restore()

        canvas.save()
        canvas.rotate(angleSecond)
        val secondHand = RectF(
            -secondHandWidth / 12,
            -radius * secondHandLength / 48 * 4/5,
            secondHandWidth / 12,
            radius * secondHandLength / 48 / 5
        )
        paint.color = secondHandColor
        paint.strokeWidth = secondHandWidth
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawRect(secondHand, paint)
        canvas.restore()
    }
}
