package com.carloshns.mychronometer.scrollablenumberpicker

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.databinding.*
import com.carloshns.mychronometer.R

class ScrollableNumberPicker: View, GestureDetector.OnGestureListener {

    companion object {
        @JvmStatic
        @BindingAdapter("number")
        fun setTime(scrollableNumberPicker: ScrollableNumberPicker, newValue: Int) {
            if (scrollableNumberPicker.number != newValue) {
                scrollableNumberPicker.number = newValue
            }
        }

        @JvmStatic
        @InverseBindingAdapter(attribute = "number")
        fun getValue(scrollableNumberPicker: ScrollableNumberPicker) = scrollableNumberPicker.number

        @JvmStatic
        @BindingAdapter("numberAttrChanged")
        fun setListeners(
                scrollableNumberPicker: ScrollableNumberPicker,
                attrChange: InverseBindingListener
        ) {
            scrollableNumberPicker.bindingListener = attrChange
        }
    }

    private val FONT_SIZE = 10F
    private val FIVE_PERCENT = 5
    private val MAX_NUMBER = 10
    private val NUMBER_VALUE = 0

    var number: Int = 0
    private var maxNumber: Int = 0

    private var barsSizePercent: Int = 0
    private var numberSize: Float = 0F
    private var barsColor: Int = 0
    private var numberColor: Int = 0

    private val topBar = Rect()
    private val bottomBar = Rect()
    private val barsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val numberPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val findSizeRect = Rect()

    private var detector: GestureDetector = GestureDetector(context, this)
    var bindingListener: InverseBindingListener? = null

    constructor(context: Context?): super(context)
    constructor(context: Context?, attributeSet: AttributeSet): super(context, attributeSet) {
        init(attributeSet)
    }

    private fun init(attributeSet: AttributeSet) {
        attributeSet?.let {
            var typedArray: TypedArray = context.obtainStyledAttributes(it, R.styleable.ScrollableNumberPicker)
            barsColor = typedArray.getColor(R.styleable.ScrollableNumberPicker_bars_color, resources.getColor(R.color.neongreenblue))
            numberColor = typedArray.getColor(R.styleable.ScrollableNumberPicker_number_color, resources.getColor(R.color.neongreenblue))
            numberSize = typedArray.getDimension(R.styleable.ScrollableNumberPicker_number_size, FONT_SIZE)
            barsSizePercent = typedArray.getInteger(R.styleable.ScrollableNumberPicker_bars_size_percent, FIVE_PERCENT)
            maxNumber = typedArray.getInteger(R.styleable.ScrollableNumberPicker_max_number, MAX_NUMBER)
            number = typedArray.getInteger(R.styleable.ScrollableNumberPicker_number, NUMBER_VALUE)
        }
        configureBarsPaint()
        configureNumberPaint()
    }

    override fun onDown(e: MotionEvent?): Boolean = true
    override fun onShowPress(e: MotionEvent?){}
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false
    override fun onLongPress(e: MotionEvent?) {}

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {

        val yInitial = e1?.y!!
        val yFinal = e2?.y!!

        if(isADownScroll(yInitial, yFinal)){
            incrementNumber()
        } else if(isAUpcroll(yInitial, yFinal)){
            decrementNumber()
        }
        verifyIfNumberIsOnLimitAndUpdate()

        postInvalidate()
        updateDataBindingValue()
        return true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return detector.onTouchEvent(event).let {
            if(!it) {
                event?.action == MotionEvent.ACTION_UP
            } else true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        configureBottomBar()
        configureTopBar()

        canvas.drawRect(topBar, barsPaint)
        canvas.drawRect(bottomBar, barsPaint)

        val pointToRender = calculatePointToRenderNumber()
        canvas.drawText(getNumberInString(), pointToRender.first, pointToRender.second, numberPaint)
    }

    private fun configureTopBar(){
        topBar.top = 0
        topBar.left = 0
        topBar.right = width
        topBar.bottom = height * barsSizePercent / 100
    }

    private fun configureBottomBar(){
        bottomBar.top = height - (height * barsSizePercent / 100)
        bottomBar.left = 0
        bottomBar.right = width
        bottomBar.bottom = height
    }

    private fun configureBarsPaint(){
        barsPaint.color = barsColor
    }

    private fun configureNumberPaint() {
        numberPaint.textSize = numberSize
        numberPaint.color = numberColor
    }

    private fun isADownScroll(yInitial: Float, yFinal: Float): Boolean {
        return yInitial < yFinal
    }

    private fun isAUpcroll(yInitial: Float, yFinal: Float): Boolean {
        return yInitial > yFinal
    }

    private fun incrementNumber(){
        number++
    }

    private fun decrementNumber(){
        number--
    }

    private fun verifyIfNumberIsOnLimitAndUpdate() {
        if (number > maxNumber) number = 0
        if (number < 0) number = maxNumber
    }

    private fun getNumberInString(): String {
        return "${if (number < 10) "0${number}" else number}"
    }

    private fun calculateNumberStringSizes(): Pair<Float, Float> {
        val numberInString = getNumberInString()
        numberPaint.getTextBounds(numberInString, 0, numberInString.length, findSizeRect)
        val textHeight = findSizeRect.height().toFloat()
        val textWidth = numberPaint.measureText(numberInString)
        return Pair(textWidth, textHeight)
    }

    private fun calculatePointToRenderNumber(): Pair<Float, Float> {
        val stringSizes = calculateNumberStringSizes()
        var largura = (width / 2) - stringSizes.first / 2
        var altura = (height / 2) + stringSizes.second / 2
        return Pair(largura, altura)
    }

    private fun updateDataBindingValue(){
        bindingListener?.onChange()
    }
}