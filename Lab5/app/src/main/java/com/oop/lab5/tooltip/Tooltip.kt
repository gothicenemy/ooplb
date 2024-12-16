package com.oop.lab5.tooltip

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.oop.lab5.R

class Tooltip(context: Context) : View(context) {
    private lateinit var tooltip: Snackbar

    fun create(text: String): Tooltip {
        tooltip = Snackbar.make((context as Activity).findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG)
        tooltip.view.setBackgroundColor(context.getColor(R.color.transparent))

        val layout = tooltip.view as ViewGroup
        val view = inflate(context, R.layout.tooltip, null).apply {
            findViewById<TextView>(R.id.tooltip_text).text = text
            findViewById<Button>(R.id.tooltip_hide).setOnClickListener {
                it.setTextColor(context.getColor(R.color.tooltip_bnt_clicked_text_color))
                hide()
            }
        }
        layout.addView(view)
        return this
    }

    fun hide() = tooltip.dismiss()

    fun display() = tooltip.show()
}