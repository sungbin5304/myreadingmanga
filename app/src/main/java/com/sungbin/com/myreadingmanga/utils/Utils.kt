package com.sungbin.com.myreadingmanga.utils

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import com.shashank.sony.fancytoastlib.FancyToast


@Suppress("DEPRECATION")
@SuppressLint("MissingPermission", "HardwareIds")
object Utils {

    var sdcard = Environment.getExternalStorageDirectory().absolutePath

    fun createFolder(name: String) {
        File("$sdcard/X-VIDEO/$name/").mkdirs()
    }

    fun toast(ctx: Context?, content: String) {
        Toast.makeText(ctx, content, Toast.LENGTH_SHORT).show()
    }

    fun read(name: String, _null: String): String {
        try {
            val file = File("$sdcard/X-VIDEO/$name/")
            if (!file.exists()) return _null
            val fis = FileInputStream(file)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            var str = br.readLine()

            while (true) {
                val inputLine = br.readLine() ?: break
                str += "\n" + inputLine
            }
            fis.close()
            isr.close()
            br.close()
            return str.toString()
        } catch (e: Exception) {
            Log.e("READ", e.toString())
        }

        return _null
    }

    fun save(name: String, content: String) {
        try {
            val file = File("$sdcard/X-VIDEO/$name")
            val fos = java.io.FileOutputStream(file)
            fos.write(content.toByteArray())
            fos.close()
        } catch (e: Exception) {
            Log.e("SAVE", e.toString())
        }

    }

    fun delete(name: String) {
        File("$sdcard/X-VIDEO/$name").delete()
    }

    fun readData(ctx: Context, name: String, _null: String): String? {
        val pref = ctx.getSharedPreferences("pref", MODE_PRIVATE)
        return pref.getString(name, _null)
    }

    fun saveData(ctx: Context, name: String, value: String) {
        val pref = ctx.getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()

        editor.putString(name, value)
        editor.apply()
    }

    fun clearData(ctx: Context) {
        val pref = ctx.getSharedPreferences("pref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

    fun copy(ctx: Context, text: String) {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.primaryClip = clip
        Toast.makeText(ctx, "클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show()
    }

    fun copy(ctx: Context, text: String, showToast: Boolean) {
        val clipboard = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.primaryClip = clip
        if (showToast) Toast.makeText(ctx, "클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show()
    }

    fun error(ctx: Context, content: String) {
        Utils.toast(ctx, content, FancyToast.LENGTH_SHORT, FancyToast.ERROR)
        Utils.copy(ctx, content)
        Log.e("Error", content)
    }

    fun error(ctx: Context, e: Exception) {
        val data = "Error: " + e + "\nLineNumber: " + e.stackTrace[0].lineNumber
        Utils.toast(ctx, "Error: $e", FancyToast.LENGTH_SHORT, FancyToast.ERROR)
        Utils.copy(ctx, data)
        Log.e("Error", data)
    }

    fun toast(ctx: Context, txt: String, length: Int, type: Int) {
        FancyToast.makeText(ctx, txt, length, type, false).show()
    }

}