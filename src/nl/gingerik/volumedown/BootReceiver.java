package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class BootReceiver extends BroadcastReceiver {

	private Logger mLog;

	@Override
	public void onReceive(Context context, Intent intent) {
		mLog = new Logger(context, BootReceiver.class.getSimpleName());
		mLog.v("Received " + intent.getAction());

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		boolean enable = sharedPref.getBoolean(SettingsActivity.PREF_ENABLE,
				context.getResources().getBoolean(R.bool.pref_enable_default));
		if (enable) {
			Intent serviceIntent = new Intent(context, BackgroundService.class);
			context.startService(serviceIntent);
		}
	}

}
