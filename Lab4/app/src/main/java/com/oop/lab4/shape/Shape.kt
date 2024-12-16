package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import com.oop.lab4.R

abstract class Shape(private val context: Context) {
    abstract val name: String
    val associatedIds = mutableMapOf<String, Int>()

    protected var startX = 0F
    protected var startY = 0F
    protected var endX = 0F
    protected var endY = 0F

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
    abstract fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?)
    abstract fun showDefault(canvas: Canvas)

    fun showRubberTrace(canvas: Canvas) {
        show(canvas, getRubberTracePaint(), null)
    }

    protected open fun getOutlinePaint() = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 7F
        color = context.getColor(R.color.black)
    }

    protected open fun getFillingPaint() = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    protected open fun getRubberTracePaint(): Paint = getOutlinePaint().apply {
        color = context.getColor(R.color.dark_blue)
        pathEffect = DashPathEffect(floatArrayOf(30F, 15F), 0F)
    }
}
