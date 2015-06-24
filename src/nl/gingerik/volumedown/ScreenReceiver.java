package nl.gingerik.volumedown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class ScreenReceiver extends BroadcastReceiver {

	private final Logger mLog;
	private PendingIntent mPendingIntent;
	private int mDelay;
	private long mElapsedTime = 0;

	public ScreenReceiver(Context context) {
		mLog = new Logger(context, ScreenReceiver.class.getSimpleName());
		mLog.v("Create ScreenReceiver");
		mDelay = 30000; // FIXME get from settings
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		mLog.v("Received " + intent.getAction());

		switch (intent.getAction()) {
		case Intent.ACTION_SCREEN_ON:
			if (mPendingIntent != null) {
				if (mElapsedTime > 0
						&& mElapsedTime < SystemClock.elapsedRealtime()) {
					mLog.v("Not cancelling alarm");
					return;
				}
				mLog.v("Cancelling alarm");
				mElapsedTime = 0;
				AlarmManager alarmManager = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				alarmManager.cancel(mPendingIntent);
				mPendingIntent = null;
			}
			break;
		case Intent.ACTION_SCREEN_OFF:
			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			Intent receiverIntent = new Intent(context, CountdownReceiver.class);
			mPendingIntent = PendingIntent.getBroadcast(context, 0,
					receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mElapsedTime = SystemClock.elapsedRealtime() + mDelay;
			mLog.v("Setting alarm");
			alarmManager.set(AlarmManager.ELAPSED_REALTIME, mElapsedTime,
					mPendingIntent);
			break;
		}
	}

}
