package com.oop.lab3.editor

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF

class EllipseShapeEditor : ShapeEditor() {

    private val centerPoint = PointF()

    override fun onFingerTouch(x: Float, y: Float) {
        centerPoint.set(x, y)
        shape.setStart(x, y)
        shape.setEnd(x, y)
    }

    override fun onFingerMove(canvas: Canvas, x: Float, y: Float) {
        val rect = RectF(
            centerPoint.x - (x - centerPoint.x),
            centerPoint.y - (y - centerPoint.y),
            x,
            y
        ).apply { sort() }

        shape.setStart(rect.left, rect.top)
        shape.setEnd(rect.right, rect.bottom)
        shape.showRubberTrace(canvas)
    }
}
