// LineShape.kt
package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint

class LineShape(private val context: Context): Shape(context) {
    override val name = context.getString(R.string.line)

    override fun isValid() = (startX != endX || startY != endY)

    override fun getInstance() = LineShape(context).also {
        it.associatedIds.putAll(this.associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        canvas.drawLine(startX, startY, endX, endY, outlinePaint)
    }

    override fun showDefault(canvas: Canvas) {
        show(canvas, getOutlinePaint(), null)
    }
}
