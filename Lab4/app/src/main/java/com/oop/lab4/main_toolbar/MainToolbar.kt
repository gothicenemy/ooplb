package com.oop.lab4.main_toolbar

import android.content.Context
import android.util.AttributeSet
import android.view.*
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.oop.lab4.R
import com.oop.lab4.my_editor.MyEditor
import com.oop.lab4.shape.Shape
import com.oop.lab4.tooltip.Tooltip

class MainToolbar(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs) {
    private lateinit var optionsMenu: PopupMenu
    private lateinit var fileSubmenu: PopupMenu
    private lateinit var objSubmenu: PopupMenu

    private lateinit var editor: MyEditor
    private lateinit var objSubmenuItems: Array<MenuItem>

    private lateinit var onObjSelectListener: (Shape) -> Unit
    private lateinit var onObjCancelListener: () -> Unit

    private lateinit var currentObjTextView: TextView

    fun onCreate(editor: MyEditor) {
        this.editor = editor
        val btnOptions = findViewById<ImageButton>(R.id.btn_options)
        currentObjTextView = findViewById(R.id.current_object)

        optionsMenu = createMenu(R.menu.main_toolbar_options_menu, btnOptions) {
            when (it.itemId) {
                R.id.file -> fileSubmenu.show()
                R.id.objects -> objSubmenu.show()
                R.id.info -> Tooltip(context, null).create(this, "Ви натиснули кнопку\n\"Довідка\"").show()
            }
        }

        fileSubmenu = createMenu(R.menu.main_toolbar_file_submenu, btnOptions) {
            when (it.itemId) {
                R.id.undo -> editor.undo()
                R.id.clear_all -> editor.clearAll()
            }
        }

        objSubmenu = createMenu(R.menu.main_toolbar_objects_submenu, btnOptions) { clickedItem ->
            objSubmenuItems.forEachIndexed { index, item ->
                if (item == clickedItem) {
                    val shape = editor.shapes[index]
                    if (!item.isChecked) onObjSelectListener(shape.getInstance()) else onObjCancelListener()
                }
            }
        }

        objSubmenuItems = objSubmenu.menu.let { menu ->
            editor.shapes.mapIndexed { index, shape ->
                menu.findItem(R.id.item_point + index).also {
                    shape.associatedIds["objSubmenuItem"] = it.itemId
                }
            }.toTypedArray()
        }
    }

    private fun createMenu(menuRes: Int, anchor: View, listener: (MenuItem) -> Unit): PopupMenu {
        return PopupMenu(context, anchor).apply {
            menuInflater.inflate(menuRes, menu)
            setOnMenuItemClickListener { listener(it); true }
        }
    }

    fun setObjListeners(onSelectListener: (Shape) -> Unit, onCancelListener: () -> Unit) {
        onObjSelectListener = onSelectListener
        onObjCancelListener = onCancelListener
    }

    fun onObjSelect(shape: Shape) {
        updateObjTextView(shape.name)
        updateSelection(shape.associatedIds["objSubmenuItem"], isSelected = true)
    }

    fun onObjCancel() {
        updateObjTextView("Не вибрано")
        updateSelection(editor.currentShape?.associatedIds?.get("objSubmenuItem"), isSelected = false)
    }

    private fun updateObjTextView(text: String) {
        currentObjTextView.text = text
    }

    private fun updateSelection(itemId: Int?, isSelected: Boolean) {
        itemId?.let { objSubmenu.menu.findItem(it).isChecked = isSelected }
    }
}