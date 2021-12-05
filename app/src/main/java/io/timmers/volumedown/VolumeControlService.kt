package io.timmers.volumedown

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.provider.Settings
import android.widget.Toast

class VolumeControlService : Service() {
    private lateinit var mLog: Logger
    private lateinit var mHandlerThread: HandlerThread
    private lateinit var mSettingsContentObserver: SettingsContentObserver

    override fun onCreate() {
        mLog = Logger(this, VolumeControlService::class.java.simpleName)
        mLog.v("Starting VolumeDown Service")
        Toast.makeText(this, "Starting VolumeDown Service", Toast.LENGTH_SHORT).show()
        super.onCreate()
        mHandlerThread = HandlerThread("SettingsContentObserverHandlerThread")
        mHandlerThread.start()
        mSettingsContentObserver = SettingsContentObserver(this, Handler(mHandlerThread.looper))
        this.applicationContext
            .contentResolver
            .registerContentObserver(
                Settings.System.CONTENT_URI,
                true,
                mSettingsContentObserver
            )
    }

    override fun onDestroy() {
        mLog.v("Stopping VolumeDown Service")
        Toast.makeText(this, "Stopping VolumeDown Service", Toast.LENGTH_SHORT).show()
        applicationContext.contentResolver.unregisterContentObserver(mSettingsContentObserver)
        mHandlerThread.quitSafely()
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mLog.v("VolumeDown Service received start command")
        return START_STICKY // keep running
    }
}