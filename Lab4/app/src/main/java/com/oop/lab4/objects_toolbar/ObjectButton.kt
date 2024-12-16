package com.oop.lab4.objects_toolbar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.view.MotionEvent
import com.oop.lab4.R
import com.oop.lab4.shape.Shape
import com.oop.lab4.tooltip.Tooltip

class ObjectButton(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageButton(context, attrs) {
    private lateinit var shape: Shape

    private var isObjSelected = false
    private lateinit var onObjSelectListener: (Shape) -> Unit
    private lateinit var onObjCancelListener: () -> Unit

    private val selectTooltip = Tooltip(context, attrs)
    private val cancelTooltip = Tooltip(context, attrs)

    private val timeOfLongPress = 1000
    private var pressStartTime: Long = 0

    fun onCreate(shape: Shape) {
        this.shape = shape
        selectTooltip.create(this, "Вибрати об'єкт\n\"${shape.name}\"")
        cancelTooltip.create(this, "Вимкнути режим\nредагування")
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                markPressed()
                pressStartTime = System.currentTimeMillis()
            }
            MotionEvent.ACTION_UP -> {
                val pressDuration = System.currentTimeMillis() - pressStartTime
                if (pressDuration < timeOfLongPress) performClick() else performLongClick()
                pressStartTime = 0
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        if (isObjSelected) onObjCancelListener() else onObjSelectListener(shape.getInstance())
        return true
    }

    override fun performLongClick(): Boolean {
        super.performLongClick()
        if (isObjSelected) {
            markSelected()
            cancelTooltip.show()
        } else {
            markNotPressed()
            selectTooltip.show()
        }
        return true
    }

    private fun markPressed() {
        backgroundTintList = context.getColorStateList(R.color.pressed_btn_background_color)
    }

    private fun markNotPressed() {
        backgroundTintList = context.getColorStateList(R.color.transparent)
    }

    private fun markSelected() {
        backgroundTintList = context.getColorStateList(R.color.selected_btn_background_color)
        colorFilter = PorterDuffColorFilter(
            context.getColor(R.color.selected_btn_icon_color),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun markNotSelected() {
        backgroundTintList = context.getColorStateList(R.color.transparent)
        colorFilter = PorterDuffColorFilter(
            context.getColor(R.color.on_objects_toolbar_color),
            PorterDuff.Mode.SRC_IN
        )
    }

    fun setObjListeners(
        onSelectListener: (Shape) -> Unit,
        onCancelListener: () -> Unit
    ) {
        onObjSelectListener = onSelectListener
        onObjCancelListener = onCancelListener
    }

    fun onObjSelect() {
        isObjSelected = true
        markSelected()
    }

    fun onObjCancel() {
        isObjSelected = false
        markNotSelected()
    }
}