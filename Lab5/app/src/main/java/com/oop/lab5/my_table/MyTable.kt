package com.oop.lab5.my_table

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.oop.lab5.R

class MyTable : Fragment(R.layout.table) {
    private var isDisplayed = false
    private val selectedRowsIndices = mutableListOf<Int>()
    private var onHideTableListener: (() -> Unit)? = null
    private var onSelectRowListener: ((Int) -> Unit)? = null
    private var onCancelRowsListener: ((List<Int>) -> Unit)? = null
    private var onDeleteRowsListener: ((List<Int>) -> Unit)? = null

    private lateinit var scrollView: ScrollView
    private lateinit var tableLayout: TableLayout
    private lateinit var bottomView: LinearLayout
    private lateinit var defaultBottomView: LinearLayout
    private lateinit var selectBottomView: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrollView = view.findViewById(R.id.table_scroll_view)
        tableLayout = view.findViewById(R.id.table_table_layout)
        bottomView = view.findViewById(R.id.files_dialog_bottom_view)

        defaultBottomView = inflateBottomView(R.layout.table_default_bottom_view) {
            findViewById<Button>(R.id.files_dialog_btn_hide).setOnClickListener {
                onHideTableListener?.invoke()
            }
        }

        selectBottomView = inflateBottomView(R.layout.table_select_bottom_view) {
            findViewById<Button>(R.id.files_dialog_btn_open).setOnClickListener {
                cancelRows(selectedRowsIndices.toList())
            }
            findViewById<Button>(R.id.files_dialog_btn_delete).setOnClickListener {
                deleteRows(selectedRowsIndices.toList())
                onDeleteRowsListener?.invoke(selectedRowsIndices.toList())
            }
        }

        bottomView.addView(defaultBottomView)
    }

    private fun inflateBottomView(layoutId: Int, init: View.() -> Unit): LinearLayout {
        return LinearLayout(context).apply {
            layoutInflater.inflate(layoutId, this, true)
            init()
        }
    }

    fun addRow(serializedShape: String) {
        val data = serializedShape.dropLast(1).split("\t")
        val row = TableRow(context).apply {
            layoutInflater.inflate(R.layout.table_row, this, true)
            findViewById<TextView>(R.id.table_shape_name).text = data[0]
            findViewById<TextView>(R.id.table_x1).text = data[1]
            findViewById<TextView>(R.id.table_y1).text = data[2]
            findViewById<TextView>(R.id.table_x2).text = data[3]
            findViewById<TextView>(R.id.table_y2).text = data[4]
            setOnClickListener { toggleRowSelection(tableLayout.indexOfChild(this)) }
        }
        tableLayout.addView(row)
        tableLayout.children.firstOrNull()?.let { if (it is TextView) tableLayout.removeView(it) }
        updateRowBgColor(row, tableLayout.indexOfChild(row))
        scrollView.scrollToDescendant(row)
    }

    private fun toggleRowSelection(index: Int) {
        if (selectedRowsIndices.contains(index)) {
            cancelRows(listOf(index))
        } else {
            selectRow(index)
        }
    }

    private fun selectRow(index: Int) {
        if (selectedRowsIndices.isEmpty()) switchBottomView(defaultBottomView, selectBottomView)
        selectedRowsIndices.add(index)
        updateRowBgColor(tableLayout.getChildAt(index), index, isSelected = true)
        onSelectRowListener?.invoke(index)
    }

    private fun cancelRows(indices: List<Int>) {
        indices.forEach { index ->
            selectedRowsIndices.remove(index)
            updateRowBgColor(tableLayout.getChildAt(index), index)
        }
        if (selectedRowsIndices.isEmpty()) switchBottomView(selectBottomView, defaultBottomView)
        onCancelRowsListener?.invoke(indices)
    }

    fun deleteRows(indices: List<Int>) {
        indices.sortedDescending().forEach {
            selectedRowsIndices.remove(it)
            tableLayout.removeViewAt(it)
        }
        refreshTableAfterDeletion()
    }

    private fun refreshTableAfterDeletion() {
        if (tableLayout.childCount == 0) {
            addEmptyTableMessage()
        } else {
            tableLayout.children.forEachIndexed { index, row -> updateRowBgColor(row, index) }
        }
        if (selectedRowsIndices.isEmpty()) switchBottomView(selectBottomView, defaultBottomView)
    }

    private fun addEmptyTableMessage() {
        tableLayout.addView(TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                resources.getDimension(R.dimen.table_content_height).toInt()
            )
            text = "Полотно порожнє"
            textSize = 20F
            setTypeface(null, Typeface.ITALIC)
            gravity = Gravity.CENTER
        })
    }

    private fun updateRowBgColor(row: View, index: Int, isSelected: Boolean = false) {
        row.setBackgroundColor(
            requireActivity().getColor(
                if (isSelected) {
                    if (index % 2 == 0) R.color.table_selected_row_bg_color_1 else R.color.table_selected_row_bg_color_2
                } else {
                    if (index % 2 == 0) R.color.table_default_row_bg_color_1 else R.color.table_default_row_bg_color_2
                }
            )
        )
    }

    private fun switchBottomView(from: View, to: View) {
        bottomView.apply {
            removeView(from)
            addView(to)
        }
    }

    fun setOnHideTableListener(listener: () -> Unit) { onHideTableListener = listener }

    fun setOnSelectRowListener(listener: (Int) -> Unit) { onSelectRowListener = listener }

    fun setOnCancelRowsListener(listener: (List<Int>) -> Unit) { onCancelRowsListener = listener }

    fun setOnDeleteRowsListener(listener: (List<Int>) -> Unit) { onDeleteRowsListener = listener }
}