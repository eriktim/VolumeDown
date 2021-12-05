package io.timmers.volumedown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Handler
import android.widget.Toast

class SettingsContentObserver(context: Context, handler: Handler?) :
    ContentObserver(handler) {
    private val mLog: Logger = Logger(context, SettingsContentObserver::class.java.simpleName)
    private var mScreenReceiver: BroadcastReceiver? = null
    private val mContext: Context
    private val mVolume: Int

    init {
        mLog.v("Create SettingsContentObserver")
        mContext = context
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        mVolume = 0 // FIXME get from settings
        if (volume != mVolume) {
            onChange(true)
        }
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val audioManager = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        if (mScreenReceiver != null) {
            mContext.unregisterReceiver(mScreenReceiver)
            mScreenReceiver = null
        }
        if (volume != mVolume) {
            mScreenReceiver = ScreenReceiver(mContext)
            mLog.v("Settings change detected")
            Toast.makeText(mContext, "Detected volume change", Toast.LENGTH_SHORT).show()
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            filter.addAction(Intent.ACTION_SCREEN_ON)
            mContext.registerReceiver(mScreenReceiver, filter)
        }
    }
}