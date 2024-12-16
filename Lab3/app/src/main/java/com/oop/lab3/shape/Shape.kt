package com.oop.lab3.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.oop.lab3.R

import com.oop.lab3.editor.ShapeEditor

package com.oop.lab3.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.oop.lab3.R

import com.oop.lab3.editor.ShapeEditor

abstract class Shape(private val context: Context) {
    abstract val name: String
    val associatedIds = mutableMapOf<String, Int>()
    abstract val editor: ShapeEditor

    protected var startX: Float = 0F
    protected var startY: Float = 0F
    protected var endX: Float = 0F
    protected var endY: Float = 0F

    fun setStart(x: Float, y: Float) {
        startX = x
        startY = y
    }

    fun setEnd(x: Float, y: Float) {
        endX = x
        endY = y
    }

    abstract fun isValid(): Boolean

    abstract fun getInstance(): Shape

    protected open fun createPaint(strokeWidth: Float, color: Int): Paint {
        return Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            color = context.getColor(color)
        }
    }

    protected open fun getOutlinePaint(): Paint = createPaint(7F, R.color.black)

    protected open fun getFillingPaint(): Paint = createPaint(0F, 0) // Default no color

    protected open fun getRubberTracePaint(): Paint = createPaint(7F, R.color.dark_blue)

    abstract fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?)

    abstract fun showDefault(canvas: Canvas)

    fun showRubberTrace(canvas: Canvas) {
        show(canvas, getRubberTracePaint(), null)
    }
}


