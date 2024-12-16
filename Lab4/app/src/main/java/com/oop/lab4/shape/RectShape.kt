package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class RectShape(private val context: Context): Shape(context) {
    override val name = context.getString(R.string.rectangle)

    override fun isValid() = (startX != endX || startY != endY)

    override fun getInstance() = RectShape(context).also {
        it.associatedIds.putAll(this.associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        val rect = RectF(startX, startY, endX, endY).apply { sort() }
        fillingPaint?.let { canvas.drawRect(rect, it) }
        canvas.drawRect(rect, outlinePaint)
    }

    override fun showDefault(canvas: Canvas) {
        show(canvas, getOutlinePaint(), null)
    }
}
