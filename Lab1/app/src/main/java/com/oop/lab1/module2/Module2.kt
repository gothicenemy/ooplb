package com.oop.lab1.module2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.oop.lab1.R
import com.oop.lab1.Module2Handler

private const val BTN_CANCEL = 0
private const val BTN_THEN = 2

class Module2 : DialogFragment(), Module2Interface {
    private lateinit var activityContext: Context
    private lateinit var resultHandler: Module2Handler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(R.string.dialog_name_module2)
        val view = layoutInflater.inflate(R.layout.dialog_module2, null)
        builder.setView(view)

        view.findViewById<Button>(R.id.btn_then_module2).setOnClickListener {
            resultHandler.handleModule2Result(BTN_THEN)
            dismiss()
        }
        view.findViewById<Button>(R.id.btn_cancel_module2).setOnClickListener {
            resultHandler.handleModule2Result(BTN_CANCEL)
            dismiss()
        }

        return builder.create()
    }

    override fun run(context: Context, manager: FragmentManager, handler: Module2Handler) {
        val module2 = Module2()
        module2.activityContext = context
        module2.resultHandler = handler
        module2.show(manager, "dialog_module2")
    }
}
