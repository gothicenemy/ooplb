package com.oop.object3

import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs
import kotlin.math.round

class Object3 : AppCompatActivity() {
    private lateinit var determinantTextView: TextView
    private var wasNewIntent = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        determinantTextView = findViewById(R.id.determinant)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        wasNewIntent = true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && wasNewIntent) {
            handleNewIntent()
            wasNewIntent = false
        }
    }

    private fun handleNewIntent() {
        if (intent.getStringExtra("SIGNAL") == "START") {
            val data = readFromClipboard()
            if (data.isNotEmpty()) {
                val determinant = calculateDeterminant(deserializeMatrix(data))
                determinantTextView.text = determinant.toString()
            } else {
                showToast("Виникла помилка читання з буфера обміну")
            }
        }
    }

    private fun deserializeMatrix(str: String): Array<IntArray> = str.split("\n").map { row ->
        row.split("\t").map { it.toInt() }.toIntArray()
    }.toTypedArray()

    private fun calculateDeterminant(matrix: Array<IntArray>): Int {
        val n = matrix.size
        if (n == 1) return matrix[0][0]
        val matrixDouble = matrix.map { row -> row.map { it.toDouble() }.toDoubleArray() }.toTypedArray()
        for (i in 0 until n - 1) {
            val maxRow = (i until n).maxByOrNull { abs(matrixDouble[it][i]) } ?: i
            if (maxRow != i) matrixDouble.swapRows(i, maxRow)
            if (matrixDouble[i][i] == 0.0) return 0
            for (j in i + 1 until n) {
                val factor = matrixDouble[j][i] / matrixDouble[i][i]
                for (k in i + 1 until n) {
                    matrixDouble[j][k] -= factor * matrixDouble[i][k]
                }
            }
        }
        return round(matrixDouble.fold(1.0) { acc, row -> acc * row[matrixDouble.indexOf(row)] }).toInt()
    }

    private fun Array<DoubleArray>.swapRows(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }

    private fun readFromClipboard(): String {
        val manager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        return manager.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
