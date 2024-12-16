package com.oop.lab5.file_manager

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.oop.lab5.R
import com.oop.lab5.tooltip.Tooltip
import java.io.*

class FileManager(private val context: Context) {
    private val fileExtension: String by lazy { context.getString(R.string.file_extension) }
    private val drawingsDir: File by lazy {
        File(context.getExternalFilesDir(null), context.getString(R.string.drawings_dir_name)).apply { if (!exists()) mkdirs() }
    }
    private lateinit var currentFile: File

    private val createFileDialog = CreateFileDialog()
    private val filesDialog = FilesDialog()

    private lateinit var onCreateFileListener: (String) -> String
    private lateinit var onOpenFileListener: (String, String) -> Unit
    private lateinit var onSaveFileListener: () -> String
    private lateinit var onDeleteFileListener: (String, String?) -> Unit

    fun onCreate(startListener: (String) -> Unit) {
        val defaultFileName = getDefaultFileName()
        currentFile = File(drawingsDir, defaultFileName)
        startListener(defaultFileName)

        createFileDialog.setFileCreationListeners({}, ::createFile)
        filesDialog.setOnFileListeners(::openFile, ::deleteFile)
    }

    fun files(manager: FragmentManager) {
        filesDialog.display(manager, drawingsDir.list())
    }

    fun save() {
        FileWriter(currentFile).use {
            it.write(onSaveFileListener())
        }
        Tooltip(context).create("Малюнок збережено у файлі ${currentFile.name}").display()
    }

    fun saveAs(manager: FragmentManager) {
        createFileDialog.display(manager, getShortFileName(getDefaultFileName()))
    }

    private fun getShortFileName(fileName: String) = fileName.removeSuffix(fileExtension)

    private fun getShortFileNames() = drawingsDir.list()?.map { getShortFileName(it) } ?: emptyList()

    private fun getDefaultFileName(): String {
        val nameStart = context.getString(R.string.default_short_file_name)
        var index = 1
        val shortFileNames = getShortFileNames()
        while ("$nameStart$index" in shortFileNames) index++
        return "$nameStart$index$fileExtension"
    }

    private fun openFile(fileName: String) {
        currentFile = File(drawingsDir, fileName)
        val content = currentFile.readText()
        onOpenFileListener(fileName, content)
    }

    private fun createFile(shortFileName: String): Pair<Boolean, String> {
        if (shortFileName.isBlank()) return false to "Порожнє ім'я"
        if (getShortFileNames().contains(shortFileName)) return false to "Використане ім'я"

        val fileName = "$shortFileName$fileExtension"
        currentFile = File(drawingsDir, fileName)
        FileWriter(currentFile).use { it.write(onCreateFileListener(fileName)) }
        return true to "Малюнок збережено у файлі $fileName"
    }

    private fun deleteFile(fileName: String) {
        val file = File(drawingsDir, fileName)
        file.delete()
        val newDefault = if (file.name != currentFile.name) null else getDefaultFileName()
        onDeleteFileListener(fileName, newDefault)
    }

    fun setOnFileListeners(
        createListener: (String) -> String,
        openListener: (String, String) -> Unit,
        saveListener: () -> String,
        deleteListener: (String, String?) -> Unit
    ) {
        onCreateFileListener = createListener
        onOpenFileListener = openListener
        onSaveFileListener = saveListener
        onDeleteFileListener = deleteListener
    }
}