package com.oop.lab4.objects_toolbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import com.oop.lab4.R
import com.oop.lab4.my_editor.MyEditor
import com.oop.lab4.shape.Shape

class ObjectsToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {
    private lateinit var editor: MyEditor
    private lateinit var objButtons: Array<ObjectButton>

    private lateinit var onObjSelectListener: (Shape) -> Unit
    private lateinit var onObjCancelListener: () -> Unit

    fun onCreate(editor: MyEditor) {
        this.editor = editor
        objButtons = editor.shapes.mapIndexed { index, shape ->
            findViewById<ObjectButton>(R.id.btn_point + index).also {
                shape.associatedIds["objButton"] = it.id
            }
        }.toTypedArray()
    }

    fun setObjListeners(
        onSelectListener: (Shape) -> Unit,
        onCancelListener: () -> Unit
    ) {
        onObjSelectListener = onSelectListener
        onObjCancelListener = onCancelListener

        objButtons.forEachIndexed { index, button ->
            val shape = editor.shapes[index]
            button.apply {
                onCreate(shape)
                setObjListeners(onSelectListener, onCancelListener)
            }
        }
    }

    fun onObjSelect(shape: Shape) {
        editor.currentShape?.associatedIds?.get("objButton")?.let {
            findViewById<ObjectButton>(it).onObjCancel()
        }
        shape.associatedIds["objButton"]?.let {
            findViewById<ObjectButton>(it).onObjSelect()
        }
    }

    fun onObjCancel() {
        editor.currentShape?.associatedIds?.get("objButton")?.let {
            findViewById<ObjectButton>(it).onObjCancel()
        }
    }
}