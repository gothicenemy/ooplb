package com.oop.lab5.file_manager

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.oop.lab5.R

class FilesDialog : DialogFragment(R.layout.files_dialog) {
    private lateinit var tableLayout: TableLayout
    private lateinit var bottomView: LinearLayout
    private var currentFileList = mutableListOf<String>()
    private var selectedRow = object {
        var view: TableRow? = null
        var fileName: String? = null
    }

    private lateinit var onOpenListener: (String) -> Unit
    private lateinit var onDeleteListener: (String) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tableLayout = view.findViewById(R.id.files_dialog_table_layout).apply {
            removeAllViews()
            if (currentFileList.isNotEmpty()) {
                currentFileList.forEach { file ->
                    val row = TableRow(context).apply {
                        layoutInflater.inflate(R.layout.files_dialog_row, this, true)
                        findViewById<TextView>(R.id.files_dialog_row_name).text = file
                        setOnClickListener { handleRowClick(this, file) }
                    }
                    addView(row)
                }
            } else {
                onEmptyDir()
            }
        }

        setupBottomView(view)
    }

    private fun handleRowClick(row: TableRow, file: String) {
        selectedRow.view?.let { cancelRow() }
        selectRow(row, file)
    }

    private fun setupBottomView(view: View) {
        bottomView = view.findViewById(R.id.files_dialog_bottom_view)
        val defaultBottomView = createBottomView(R.layout.files_dialog_default_bottom_view).apply {
            findViewById<Button>(R.id.files_dialog_btn_hide).setOnClickListener { dismiss() }
        }
        val selectBottomView = createBottomView(R.layout.files_dialog_select_bottom_view).apply {
            findViewById<Button>(R.id.files_dialog_btn_open).setOnClickListener {
                onOpenListener(selectedRow.fileName!!)
                cancelRow()
                dismiss()
            }
            findViewById<Button>(R.id.files_dialog_btn_delete).setOnClickListener { deleteRow() }
        }

        bottomView.addView(defaultBottomView)
    }

    private fun createBottomView(layoutRes: Int): LinearLayout {
        return LinearLayout(context).apply {
            layoutInflater.inflate(layoutRes, this, true)
        }
    }

    private fun selectRow(row: TableRow, file: String) {
        bottomView.removeAllViews()
        bottomView.addView(createBottomView(R.layout.files_dialog_select_bottom_view))
        row.setBackgroundColor(requireActivity().getColor(R.color.files_dialog_selected_row_bg_color))
        selectedRow.view = row
        selectedRow.fileName = file
    }

    private fun cancelRow() {
        bottomView.removeAllViews()
        bottomView.addView(createBottomView(R.layout.files_dialog_default_bottom_view))
        selectedRow.view?.setBackgroundColor(requireActivity().getColor(R.color.files_dialog_default_row_bg_color))
        selectedRow = object { var view: TableRow? = null; var fileName: String? = null }
    }

    private fun deleteRow() {
        bottomView.removeAllViews()
        bottomView.addView(createBottomView(R.layout.files_dialog_default_bottom_view))
        currentFileList.remove(selectedRow.fileName)
        tableLayout.removeView(selectedRow.view)
        onDeleteListener(selectedRow.fileName!!)
        selectedRow = object { var view: TableRow? = null; var fileName: String? = null }
        if (currentFileList.isEmpty()) onEmptyDir()
    }

    private fun onEmptyDir() {
        tableLayout.addView(TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, resources.getDimension(R.dimen.files_dialog_content_height).toInt())
            gravity = Gravity.CENTER
            text = getString(R.string.files_dialog_default_text)
            textSize = 20F
            setTypeface(null, Typeface.ITALIC)
        })
    }

    fun display(manager: FragmentManager, fileList: Array<String>?) {
        currentFileList.clear()
        fileList?.let { currentFileList.addAll(it) }
        show(manager, "files_dialog")
    }

    fun setOnFileListeners(openListener: (String) -> Unit, deleteListener: (String) -> Unit) {
        onOpenListener = openListener
        onDeleteListener = deleteListener
    }
}
