package com.oop.object2

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

class Object2 : AppCompatActivity() {
    private lateinit var matrixTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        matrixTextView = findViewById(R.id.matrix)
        handleIntent()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        intent.getIntArrayExtra("DATA")?.let { (n, min, max) ->
            try {
                val matrix = generateMatrix(n, min, max)
                showMatrix(matrix)
                writeToClipboard(serializeMatrix(matrix))
                sendTaskEndingSignal(0)
            } catch (e: Exception) {
                sendTaskEndingSignal(1)
            }
        }
    }

    private fun generateMatrix(n: Int, min: Int, max: Int): Array<IntArray> = Array(n) {
        IntArray(n) { (min..max).random() }
    }

    private fun showMatrix(matrix: Array<IntArray>) {
        matrixTextView.text = matrix.joinToString("\n\n") { row ->
            row.joinToString(" ") { value ->
                val prefix = if (value >= 0) " " else ""
                val spaces = when (abs(value)) {
                    in 0..9 -> "      "
                    in 10..99 -> "    "
                    in 100..999 -> "  "
                    else -> " "
                }
                "$prefix$value$spaces"
            }.trimEnd()
        }
    }

    private fun serializeMatrix(matrix: Array<IntArray>) = matrix.joinToString("\n") {
        it.joinToString("\t")
    }

    private fun writeToClipboard(data: String) {
        (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
            ClipData.newPlainText("MATRIX", data)
        )
    }

    private fun sendTaskEndingSignal(status: Int) {
        sendBroadcast(Intent("OBJECT2_SEND_SIGNAL").apply {
            putExtra("SIGNAL", if (status == 0) "TASK_END_SUCCESS" else "TASK_END_FAILURE")
        })
    }
}