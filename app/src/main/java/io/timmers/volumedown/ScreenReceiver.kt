package io.timmers.volumedown

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class ScreenReceiver(context: Context) : BroadcastReceiver() {
    private val mLog: Logger = Logger(context, ScreenReceiver::class.java.simpleName)
    private var mPendingIntent: PendingIntent? = null
    private val mDelay: Int
    private var mElapsedTime: Long = 0

    init {
        mLog.v("Create ScreenReceiver")
        mDelay = 30000 // FIXME get from settings
    }

    override fun onReceive(context: Context, intent: Intent) {
        mLog.v("Received " + intent.action)
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> if (mPendingIntent != null) {
                if (mElapsedTime > 0 && mElapsedTime < SystemClock.elapsedRealtime()) {
                    mLog.v("Not cancelling alarm")
                    return
                }
                mLog.v("Cancelling alarm")
                mElapsedTime = 0
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(mPendingIntent)
                mPendingIntent = null
            }
            Intent.ACTION_SCREEN_OFF -> {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val receiverIntent = Intent(context, CountdownReceiver::class.java)
                mPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    receiverIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                mElapsedTime = SystemClock.elapsedRealtime() + mDelay
                mLog.v("Setting alarm")
                alarmManager[AlarmManager.ELAPSED_REALTIME, mElapsedTime] = mPendingIntent
            }
        }
    }
}