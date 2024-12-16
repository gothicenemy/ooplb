package com.oop.lab1.module1

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.oop.lab1.R
import com.oop.lab1.Module1Handler

private const val BTN_CANCEL = 0
private const val BTN_CONFIRM = 1

class Module1 : DialogFragment(), Module1Interface {
    private var result: Int? = null
    private lateinit var activityContext: Context
    private lateinit var resultHandler: Module1Handler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(R.string.dialog_name_module1)
        val view = layoutInflater.inflate(R.layout.dialog_module1, null)
        builder.setView(view)
        
        val textView = view.findViewById<TextView>(R.id.current_number_module1)
        val seekBar = view.findViewById<SeekBar>(R.id.seek_bar_module1)
        seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                result = progress + 1
                textView.text = "$result"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
                if (result == null) result = 1
                textView.text = "$result"
                textView.textSize = 30f
                textView.setTextColor(ContextCompat.getColor(activityContext, R.color.black))
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                textView.textSize = 20f
            }
        })

        view.findViewById<Button>(R.id.btn_confirm_module1).setOnClickListener {
            resultHandler.handleModule1Result(BTN_CONFIRM, result)
            dismiss()
        }
        view.findViewById<Button>(R.id.btn_cancel_module1).setOnClickListener {
            resultHandler.handleModule1Result(BTN_CANCEL, result)
            dismiss()
        }

        return builder.create()
    }

    override fun run(context: Context, manager: FragmentManager, handler: Module1Handler) {
        val module1 = Module1()
        module1.activityContext = context
        module1.resultHandler = handler
        module1.show(manager, "dialog_module1")
    }
}
