package com.oop.lab4.my_editor

import android.content.Context
import com.oop.lab4.paint_view.PaintUtils
import com.oop.lab4.shape.*

class MyEditor(context: Context) : PaintMessagesHandler {

    override var isRubberTraceModeOn = false

    lateinit var paintUtils: PaintUtils
    private val drawnShapes = mutableListOf<Shape>()

    private val shapes = arrayOf(
        PointShape(context),
        LineShape(context),
        RectShape(context),
        EllipseShape(context),
        SegmentShape(context),
        CuboidShape(context)
    )

    var currentShape: Shape? = null
        private set

    fun start(shape: Shape) {
        currentShape = shape
    }

    fun close() {
        currentShape = null
    }

    override fun onFingerTouch(x: Float, y: Float) {
        currentShape?.apply {
            setStart(x, y)
            setEnd(x, y)
        }
    }

    override fun onFingerMove(x: Float, y: Float) {
        currentShape?.let {
            isRubberTraceModeOn = true
            paintUtils.clearCanvas(paintUtils.rubberTraceCanvas)
            it.setEnd(x, y)
            it.showRubberTrace(paintUtils.rubberTraceCanvas)
            paintUtils.repaint()
        }
    }

    override fun onFingerRelease() {
        currentShape?.let {
            isRubberTraceModeOn = false
            if (it.isValid()) drawnShapes.add(it)
            paintUtils.repaint()
            currentShape = it.getInstance()
        }
    }

    override fun onPaint() {
        paintUtils.clearCanvas(paintUtils.rubberTraceCanvas)
        paintUtils.clearCanvas(paintUtils.drawnShapesCanvas)
        drawnShapes.forEach { it.showDefault(paintUtils.drawnShapesCanvas) }
    }

    fun undo() {
        if (drawnShapes.isNotEmpty()) {
            drawnShapes.removeLast()
            paintUtils.repaint()
        }
    }

    fun clearAll() {
        if (drawnShapes.isNotEmpty()) {
            drawnShapes.clear()
            paintUtils.repaint()
        }
    }
}