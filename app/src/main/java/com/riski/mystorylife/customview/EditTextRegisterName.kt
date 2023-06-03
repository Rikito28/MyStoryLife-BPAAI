package com.riski.mystorylife.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.riski.mystorylife.R

class EditTextRegisterName : AppCompatEditText, View.OnTouchListener {
    private var isFocusedendEdit = false

    constructor(context: Context) : super(context) {
    init()
    }
    constructor(context: Context, Attrs: AttributeSet) : super(context, Attrs) {
        init()
    }

    constructor(context: Context, Attrs: AttributeSet, defStyleAttr: Int) : super(context, Attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.name_hint)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showError() {
        error = context.getString(R.string.name_error)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return false
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) showError()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isFocused) {
                setBackgroundResource(R.drawable.ic_bg_email_click)
            }}
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isFocusedendEdit) {
                    setBackgroundResource(R.drawable.ic_bg_email)
                }}
        }
        return super.onTouchEvent(event)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused && !isFocusedendEdit) {
            setBackgroundResource(R.drawable.ic_bg_email)
        }
    }
}