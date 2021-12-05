package io.timmers.volumedown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class CountdownReceiver : BroadcastReceiver() {
    private lateinit var mLog: Logger
    private var mVolume = 0

    override fun onReceive(context: Context, intent: Intent) {
        mLog = Logger(context, CountdownReceiver::class.java.simpleName)
        mLog.v("Received end of countdown")
        mVolume = 0 // FIXME get from settings
        mLog.i("Reset volume to $mVolume")
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, AudioManager.FLAG_SHOW_UI)
    }
}