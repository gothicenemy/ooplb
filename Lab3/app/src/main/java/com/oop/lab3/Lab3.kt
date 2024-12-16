package com.oop.lab3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.oop.lab3.shape_editor.ShapeObjectsEditor
import com.oop.lab3.shape.Shape
import com.oop.lab3.paint_view.PaintView
import com.oop.lab3.main_toolbar.MainToolbar
import com.oop.lab3.objects_toolbar.ObjectsToolbar

class Lab3 : AppCompatActivity() {
    private lateinit var shapeObjEditor: ShapeObjectsEditor
    private lateinit var mainToolbar: MainToolbar
    private lateinit var objectsToolbar: ObjectsToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        shapeObjEditor = ShapeObjectsEditor(this)

        setupToolbar(mainToolbar, shapeObjEditor)
        setupToolbar(objectsToolbar, shapeObjEditor)

        val paintView = findViewById<PaintView>(R.id.paint_view).apply {
            handler = shapeObjEditor
            shapeObjEditor.paintUtils = this
        }
    }

    private fun setupToolbar(toolbar: ObjectsToolbar, editor: ShapeObjectsEditor) {
        toolbar.onCreate(editor)
        toolbar.setObjListeners(::onObjSelect, ::onObjCancel)
    }

    private fun onObjSelect(shape: Shape) {
        mainToolbar.onObjSelect(shape)
        objectsToolbar.onObjSelect(shape)
        shapeObjEditor.startEditor(shape)
    }

    private fun onObjCancel() {
        mainToolbar.onObjCancel()
        objectsToolbar.onObjCancel()
        shapeObjEditor.closeEditor()
    }
}

