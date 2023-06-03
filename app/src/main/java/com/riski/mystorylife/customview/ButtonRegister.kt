package com.riski.mystorylife.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.riski.mystorylife.R

class ButtonRegister : AppCompatButton {
    private lateinit var yesBackground : Drawable
    private lateinit var noBackground : Drawable
    private var colorText : Int = 0
    private lateinit var yesFill : String
    private lateinit var noFill: String

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (isEnabled) yesBackground else noBackground

        setTextColor(colorText)
        textSize = 16f
        gravity = Gravity.CENTER
        text = if (isEnabled) yesFill else noFill
    }

    private fun init() {
        colorText = ContextCompat.getColor(context, android.R.color.background_light)
        yesBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        noBackground = ContextCompat.getDrawable(context, R.drawable.ic_bg_button_off) as Drawable
        yesFill = resources.getString(R.string.first_register)
        noFill = resources.getString(R.string.no_fill)
    }
}