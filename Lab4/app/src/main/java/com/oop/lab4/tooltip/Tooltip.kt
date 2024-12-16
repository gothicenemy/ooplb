package com.oop.lab4.tooltip

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.oop.lab4.R

class Tooltip(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private lateinit var tooltip: Snackbar

    fun create(parent: View, text: String): Tooltip {
        tooltip = Snackbar.make(parent, "", Snackbar.LENGTH_LONG).apply {
            view.setBackgroundColor(context.getColor(R.color.transparent))
            (view as Snackbar.SnackbarLayout).apply {
                val customView = inflate(context, R.layout.tooltip, null)
                addView(customView)

                customView.findViewById<TextView>(R.id.tooltip_text).text = text
                customView.findViewById<Button>(R.id.tooltip_hide).apply {
                    setOnClickListener {
                        setTextColor(context.getColor(R.color.tooltip_bnt_clicked_text_color))
                        hide()
                    }
                }
            }
        }
        return this
    }

    fun hide() {
        tooltip.dismiss()
    }

    fun show() {
        tooltip.show()
    }
}