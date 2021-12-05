package io.timmers.volumedown

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    private lateinit var mLog: Logger

    override fun onReceive(context: Context, intent: Intent) {
        mLog = Logger(context, BootReceiver::class.java.simpleName)
        mLog.v("Received " + intent.action)
        val serviceIntent = Intent(context, VolumeControlService::class.java)
        context.startService(serviceIntent)
    }
}