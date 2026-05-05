package com.flosun.pomodoro.core.functions

import android.content.Context
import android.widget.Toast
import androidx.core.net.toUri

object InternalStorageFuncs {
    fun copyImageToInternalStorageApp(
        context: Context,
        uri: String,
        path: String,
    ): String {
        try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri.toUri())
                ?: throw Exception("Failed to open input stream from URI: $uri")

            val file = java.io.File(path).apply { parentFile?.mkdirs() }
            inputStream.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            return path
        } catch (exception: Exception) {
            exception.printStackTrace()
            Toast.makeText(
                context.applicationContext,
                "Failed to copy image to internal storage. Please try again.",
                Toast.LENGTH_SHORT
            ).show()

            return ""
        }
    }

    fun deleteFileFromInternalStorage(path: String): Boolean {
        return try {
            val file = java.io.File(path)
            if (file.exists()) {
                file.delete()
            } else {
                false
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            false
        }
    }
}