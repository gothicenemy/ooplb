package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class EllipseShape(private val context: Context): Shape(context) {
    override val name = context.getString(R.string.ellipse)

    override fun isValid() = (startX != endX || startY != endY)

    override fun getInstance() = EllipseShape(context).also {
        it.associatedIds.putAll(this.associatedIds)
    }

    override fun getFillingPaint() = super.getFillingPaint().apply {
        color = context.getColor(R.color.light_green)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        val rect = RectF(startX, startY, endX, endY).apply { sort() }
        fillingPaint?.let { canvas.drawOval(rect, it) }
        canvas.drawOval(rect, outlinePaint)
    }

    override fun showDefault(canvas: Canvas) {
        show(canvas, getOutlinePaint(), getFillingPaint())
    }
}
