package com.oop.lab5.objects_toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.oop.lab5.R
import com.oop.lab5.my_editor.MyEditor
import com.oop.lab5.shape.Shape

class ObjectsToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {
    private lateinit var editor: MyEditor
    private lateinit var objButtons: Array<ObjectButton>

    fun initialize(editor: MyEditor,
                   onSelectListener: (Shape) -> Unit,
                   onCancelListener: () -> Unit) {
        this.editor = editor
        objButtons = arrayOf(
            findViewById(R.id.btn_point),
            findViewById(R.id.btn_line),
            findViewById(R.id.btn_rectangle),
            findViewById(R.id.btn_ellipse),
            findViewById(R.id.btn_segment),
            findViewById(R.id.btn_cuboid),
        )

        objButtons.forEachIndexed { index, button ->
            val shape = editor.shapes[index]
            shape.associatedIds["objButton"] = button.id
            button.setup(shape, onSelectListener, onCancelListener)
        }
    }

    fun onSelectObj(shape: Shape) {
        editor.currentShape?.associatedIds?.get("objButton")?.let { id ->
            findViewById<ObjectButton>(id)?.onCancelObj()
        }
        shape.associatedIds["objButton"]?.let { id ->
            findViewById<ObjectButton>(id)?.onSelectObj()
        }
    }

    fun onCancelObj() {
        editor.currentShape?.associatedIds?.get("objButton")?.let { id ->
            findViewById<ObjectButton>(id)?.onCancelObj()
        }
    }
}
