package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.oop.lab4.R

class PointShape(private val context: Context): Shape(context) {
    override val name = context.getString(R.string.point)

    override fun isValid() = true

    override fun getInstance() = PointShape(context).also {
        it.associatedIds.putAll(this.associatedIds)
    }

    override fun getOutlinePaint() = super.getOutlinePaint().apply {
        strokeWidth = 15F
    }

    override fun getRubberTracePaint() = super.getRubberTracePaint().apply {
        strokeWidth = 15F
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        canvas.drawPoint(startX, startY, outlinePaint)
    }

    override fun showDefault(canvas: Canvas) {
        show(canvas, getOutlinePaint(), null)
    }
}
