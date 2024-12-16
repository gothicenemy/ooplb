package com.oop.lab4.shape

import android.content.Context
import android.graphics.*
import com.oop.lab4.R

class CuboidShape(private val context: Context) : Shape(context), LineShapeInterface, RectShapeInterface {
    override val name = context.getString(R.string.cuboid)

    override fun isValid() = startX != endX || startY != endY

    override fun getInstance() = CuboidShape(context).also {
        it.associatedIds.putAll(associatedIds)
    }

    override fun show(canvas: Canvas, outlinePaint: Paint, fillingPaint: Paint?) {
        val offset = 100F
        val frontRect = RectF(startX, startY, endX, endY).apply { sort() }
        val backRect = RectF(frontRect).apply { offset(offset, -offset); sort() }

        rectShapeShow(context, canvas, outlinePaint, null, frontRect)
        rectShapeShow(context, canvas, outlinePaint, null, backRect)

        listOf(
            frontRect.right to backRect.right,
            frontRect.bottom to backRect.bottom,
            frontRect.left to backRect.left,
            frontRect.top to backRect.top
        ).zipWithNext().forEach { (start, end) ->
            lineShapeShow(context, canvas, outlinePaint, PointF(start, start), PointF(end, end))
        }
    }

    override fun showDefault(canvas: Canvas) = show(canvas, getOutlinePaint(), null)
}