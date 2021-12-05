package io.timmers.volumedown

import android.content.Context
import android.util.Log
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger as NativeLogger

class Logger(private val mContext: Context, private val mTag: String) {
    private val mFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm'Z'", Locale.US
    )
    private final val mNativeLogger: NativeLogger = NativeLogger.getLogger(mTag)
    private var mFile: File? = null

    init {
        setLogFile()
    }

    private fun setLogFile(): Boolean {
        val externalStoragePath = mContext.getExternalFilesDir(null)
        if (externalStoragePath == null) {
            Log.w(mTag, "External storage is not ready. Not writing to log file.")
            return false
        }
        mFile = File(externalStoragePath.path + "/volumedown.log")
        return true
    }

    fun v(message: String) {
        Log.v(mTag, message)
        appendLog(Level.FINE, message)
    }

    fun d(message: String) {
        Log.d(mTag, message)
        appendLog(Level.FINER, message)
    }

    fun i(message: String) {
        Log.i(mTag, message)
        appendLog(Level.INFO, message)
    }

    fun e(message: String) {
        Log.e(mTag, message)
        appendLog(Level.SEVERE, message)
    }

    private fun appendLog(level: Level, message: String) {
        mNativeLogger.log(level, "[${level.name}] $message")
        if (mFile == null) {
            setLogFile()
            if (mFile == null) {
                return
            }
        }
        if (!mFile!!.exists()) {
            try {
                mFile!!.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            val buf = BufferedWriter(FileWriter(mFile, true))
            buf.append(mFormat.format(Date()))
            buf.append("\t" + level)
            buf.append("\t" + mTag)
            buf.append("\t" + message)
            buf.newLine()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}