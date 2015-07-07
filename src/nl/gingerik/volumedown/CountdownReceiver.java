package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;

public class CountdownReceiver extends BroadcastReceiver {

	private Logger mLog;
	private int mVolume;

	@Override
	public void onReceive(Context context, Intent intent) {
		mLog = new Logger(context, CountdownReceiver.class.getSimpleName());
		mLog.v("Received end of countdown");

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);

		mVolume = sharedPref
				.getInt(SettingsActivity.PREF_VOLUME_LEVEL,
						context.getResources().getInteger(
								R.integer.pref_volume_level_default));
		mLog.i("Reset volume to " + mVolume);

		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume,
				AudioManager.FLAG_SHOW_UI);
	}

}
