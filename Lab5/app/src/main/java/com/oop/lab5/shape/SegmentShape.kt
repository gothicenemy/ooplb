package com.oop.lab5.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import com.oop.lab5.R
import kotlin.math.*

class SegmentShape(private val context: Context) : Shape(context), ShapeInterface {
    override val name = context.getString(R.string.segment)

    override fun isValid() = startX != endX || startY != endY

    override fun getInstance(): Shape = SegmentShape(context).also {
        it.associatedIds.putAll(associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        if (!isValid()) return

        val ellipseRadius = 50F
        val startPoint = PointF(startX, startY)
        val endPoint = PointF(endX, endY)
        val distance = hypot(endX - startX, endY - startY)
        val angle = acos((endX - startX) / distance)
        val offset = PointF(ellipseRadius * cos(angle), ellipseRadius * sin(angle))

        val tangentPoints = listOf(
            PointF(startX + offset.x, startY + offset.y),
            PointF(endX - offset.x, endY - offset.y)
        )

        showLine(context, canvas, outlinePaint, tangentPoints[0], tangentPoints[1])
        showEllipse(context, canvas, outlinePaint, null, startPoint, ellipseRadius)
        showEllipse(context, canvas, outlinePaint, null, endPoint, ellipseRadius)
    }

    override fun showDefault(canvas: Canvas) = show(canvas, getOutlinePaint("default"), null)

    override fun showSelected(canvas: Canvas) = show(canvas, getOutlinePaint("selected"), null)
}