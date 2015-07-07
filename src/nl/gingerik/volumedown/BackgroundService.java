package nl.gingerik.volumedown;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class BackgroundService extends Service {

	private Logger mLog;
	private SystemSettingsObserver mSettingsContentObserver;
	private boolean mShowToast;

	@Override
	public void onCreate() {
		mLog = new Logger(this, BackgroundService.class.getSimpleName());
		mLog.v("Starting VolumeDown Service");

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		mShowToast = sharedPref.getBoolean(SettingsActivity.PREF_TOAST,
				getResources().getBoolean(R.bool.pref_toast_default));

		if (mShowToast) {
			Toast.makeText(this, "Starting VolumeDown Service",
					Toast.LENGTH_SHORT).show();
		}

		super.onCreate();
		mSettingsContentObserver = new SystemSettingsObserver(this,
				new Handler());
		this.getApplicationContext()
				.getContentResolver()
				.registerContentObserver(
						android.provider.Settings.System.CONTENT_URI, true,
						mSettingsContentObserver);
	}

	@Override
	public void onDestroy() {
		mLog.v("Stopping VolumeDown Service");
		if (mShowToast) {
			Toast.makeText(this, "Stopping VolumeDown Service",
					Toast.LENGTH_SHORT).show();
		}

		if (mSettingsContentObserver != null) {
			getApplicationContext().getContentResolver()
					.unregisterContentObserver(mSettingsContentObserver);
			mSettingsContentObserver = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mLog.v("VolumeDown Service received start command");
		return START_STICKY; // keep running
	}

}
