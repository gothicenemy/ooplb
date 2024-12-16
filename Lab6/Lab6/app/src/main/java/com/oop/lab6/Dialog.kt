package com.oop.lab6

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class Dialog : DialogFragment() {
    private lateinit var onConfirmListener: (IntArray) -> Unit

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.dialog, null)
        val nEditText: EditText = view.findViewById(R.id.enter_n)
        val minEditText: EditText = view.findViewById(R.id.enter_min)
        val maxEditText: EditText = view.findViewById(R.id.enter_max)

        return AlertDialog.Builder(requireActivity())
            .setTitle(R.string.dialog_header_text)
            .setView(view)
            .create().apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                view.findViewById<Button>(R.id.dialog_cancel).setOnClickListener { dismiss() }
                view.findViewById<Button>(R.id.dialog_okay).setOnClickListener {
                    validateInput(nEditText.text.toString(), minEditText.text.toString(), maxEditText.text.toString())?.let {
                        dismiss()
                        Handler(Looper.getMainLooper()).postDelayed({ onConfirmListener(it) }, 100L)
                    }
                }
            }
    }

    private fun validateInput(n: String, min: String, max: String): IntArray? {
        val parsedN = n.toIntOrNull().takeIf { it != null && it > 0 } ?: return showError("n має бути більше 0")
        val parsedMin = min.toIntOrNull() ?: return showError("Min не є числом")
        val parsedMax = max.toIntOrNull() ?: return showError("Max не є числом")
        if (parsedMin > parsedMax) return showError("Min не може бути більшим за Max")
        return intArrayOf(parsedN, parsedMin, parsedMax)
    }

    private fun showError(message: String): IntArray? {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
        return null
    }

    fun setOnConfirmListener(listener: (IntArray) -> Unit) {
        onConfirmListener = listener
    }
}