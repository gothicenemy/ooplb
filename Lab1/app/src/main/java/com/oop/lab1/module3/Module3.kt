package com.oop.lab1.module3

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.oop.lab1.R
import com.oop.lab1.Module3Handler

private const val BTN_CANCEL = 0
private const val BTN_CONFIRM = 1
private const val BTN_BACK = 3

class Module3 : DialogFragment(), Module3Interface {
    private lateinit var activityContext: Context
    private lateinit var resultListener: Module3Handler

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activityContext)
        builder.setTitle(R.string.dialog_name_module3)
        val view = layoutInflater.inflate(R.layout.dialog_module3, null)
        builder.setView(view)

        view.findViewById<Button>(R.id.btn_back_module3).setOnClickListener {
            resultListener.handleModule3Result(BTN_BACK)
            dismiss()
        }
        view.findViewById<Button>(R.id.btn_confirm_module3).setOnClickListener {
            resultListener.handleModule3Result(BTN_CONFIRM)
        }
        view.findViewById<Button>(R.id.btn_cancel_module3).setOnClickListener {
            resultListener.handleModule3Result(BTN_CANCEL)
            dismiss()
        }

        return builder.create()
    }

    override fun run(context: Context, manager: FragmentManager, handler: Module3Handler) {
        val module3 = Module3()
        module3.activityContext = context
        module3.resultListener = handler
        module3.show(manager, "dialog_module2")
    }
}
