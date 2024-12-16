// SegmentShape.kt
package com.oop.lab4.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SegmentShape(private val context: Context): Shape(context) {
    override val name = context.getString(R.string.segment)

    override fun isValid() = (startX != endX || startY != endY)

    override fun getInstance() = SegmentShape(context).also {
        it.associatedIds.putAll(this.associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        val ellipseRadius = 50F
        val distance = sqrt((endX - startX).pow(2) + (endY - startY).pow(2))
        val angle = acos((endX - startX) / distance)
        val offset = PointF(ellipseRadius * cos(angle), ellipseRadius * sin(angle))

        // Draw main segment
        canvas.drawLine(startX + offset.x, startY + offset.y, endX - offset.x, endY - offset.y, outlinePaint)

        // Draw ellipses
        canvas.drawCircle(startX, startY, ellipseRadius, outlinePaint)
        canvas.drawCircle(endX, endY, ellipseRadius, outlinePaint)
    }

    override fun showDefault(canvas: Canvas) {
        show(canvas, getOutlinePaint(), null)
    }
}
