package com.oop.lab4.paint_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.oop.lab4.shape_editor.PaintMessagesHandler

class PaintView(context: Context, attrs: AttributeSet?) :
    View(context, attrs),
    PaintUtils {

    override lateinit var drawnShapesCanvas: Canvas
    override lateinit var rubberTraceCanvas: Canvas

    private lateinit var drawnShapesBitmap: Bitmap
    private lateinit var rubberTraceBitmap: Bitmap

    lateinit var handler: PaintMessagesHandler

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initBitmaps(w, h)
    }

    private fun initBitmaps(w: Int, h: Int) {
        drawnShapesBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawnShapesCanvas = Canvas(drawnShapesBitmap)

        rubberTraceBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        rubberTraceCanvas = Canvas(rubberTraceBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(drawnShapesBitmap, 0F, 0F, null)
        if (handler.isRubberTraceModeOn) {
            canvas.drawBitmap(rubberTraceBitmap, 0F, 0F, null)
        } else {
            handler.onPaint()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> handler.onFingerTouch(event.x, event.y)
            MotionEvent.ACTION_MOVE -> handler.onFingerMove(event.x, event.y)
            MotionEvent.ACTION_UP -> handler.onFingerRelease()
        }
        return true
    }

    override fun repaint() {
        invalidate()
    }

    override fun clearCanvas(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
    }
}
