package com.oop.lab5.shape

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import com.oop.lab5.R

class CuboidShape(private val context: Context) : Shape(context), ShapeInterface {
    override val name = context.getString(R.string.cuboid)

    override fun isValid() = startX != endX || startY != endY

    override fun getInstance(): Shape = CuboidShape(context).also {
        it.associatedIds.putAll(associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        val frontRect = RectF(startX, startY, endX, endY)
        val backRect = RectF(frontRect).apply { offset(100F, -100F) }

        listOf(frontRect, backRect).forEach { rect ->
            showRect(context, canvas, outlinePaint, null, rect)
        }

        listOf(
            PointF(frontRect.right, frontRect.top) to PointF(backRect.right, backRect.top),
            PointF(frontRect.right, frontRect.bottom) to PointF(backRect.right, backRect.bottom),
            PointF(frontRect.left, frontRect.bottom) to PointF(backRect.left, backRect.bottom),
            PointF(frontRect.left, frontRect.top) to PointF(backRect.left, backRect.top)
        ).forEach { (start, end) ->
            showLine(context, canvas, outlinePaint, start, end)
        }
    }

    override fun showDefault(canvas: Canvas) = show(canvas, getOutlinePaint("default"), null)

    override fun showSelected(canvas: Canvas) = show(canvas, getOutlinePaint("selected"), null)
}
