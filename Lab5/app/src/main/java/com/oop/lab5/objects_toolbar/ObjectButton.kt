package com.oop.lab5.objects_toolbar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.MotionEvent
import com.oop.lab5.R
import com.oop.lab5.shape.Shape
import com.oop.lab5.tooltip.Tooltip

class ObjectButton(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageButton(context, attrs) {
    private lateinit var shape: Shape
    private var isSelected = false
    private lateinit var onSelectListener: (Shape) -> Unit
    private lateinit var onCancelListener: () -> Unit

    private val selectTooltip by lazy { Tooltip(context).apply { create("") } }
    private val cancelTooltip by lazy { Tooltip(context).apply { create("") } }

    private val longPressThreshold = 1000L
    private var pressStartTime: Long = 0

    fun setup(shape: Shape,
              onSelectListener: (Shape) -> Unit,
              onCancelListener: () -> Unit) {
        this.shape = shape
        this.onSelectListener = onSelectListener
        this.onCancelListener = onCancelListener

        selectTooltip.updateText("Select: ${shape.name}")
        cancelTooltip.updateText("Cancel editing")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                markPressed()
                pressStartTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - pressStartTime
                if (pressDuration < longPressThreshold) performClick() else performLongClick()
                resetPressTime()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (isSelected) onCancelListener() else onSelectListener(shape)
        return true
    }

    override fun performLongClick(): Boolean {
        super.performLongClick()
        if (isSelected) cancelTooltip.display() else selectTooltip.display()
        return true
    }

    private fun resetPressTime() {
        pressStartTime = 0
    }

    private fun markPressed() {
        setBackgroundTint(R.color.pressed_btn_background_color)
    }

    private fun setBackgroundTint(colorRes: Int) {
        backgroundTintList = context.getColorStateList(colorRes)
    }

    private fun setIconTint(colorRes: Int) {
        colorFilter = PorterDuffColorFilter(
            context.getColor(colorRes), PorterDuff.Mode.SRC_IN
        )
    }

    fun onSelectObj() {
        isSelected = true
        setBackgroundTint(R.color.selected_btn_background_color)
        setIconTint(R.color.selected_btn_icon_color)
    }

    fun onCancelObj() {
        isSelected = false
        setBackgroundTint(R.color.transparent)
        setIconTint(R.color.on_objects_toolbar_color)
    }
}
