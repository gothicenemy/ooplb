package com.oop.lab3.editor

import com.oop.lab2.shape.Shape

abstract class ShapeEditor : Editor() {

    lateinit var shape: Shape

    override fun onFingerRelease(drawnShapes: MutableList<Shape>) {
        shape.takeIf { it.isValid() }?.let(drawnShapes::add)
        shape = shape.getInstance()
    }
}