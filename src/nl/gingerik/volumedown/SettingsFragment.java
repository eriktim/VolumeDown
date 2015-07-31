package nl.gingerik.volumedown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private SharedPreferences mSharedPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		mSharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		onSharedPreferenceChanged(mSharedPref, SettingsActivity.PREF_ENABLE);
		onSharedPreferenceChanged(mSharedPref, SettingsActivity.PREF_TIMEOUT);
	}

	@Override
	public void onResume() {
		super.onResume();
		mSharedPref.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		if (mSharedPref != null) {
			mSharedPref.unregisterOnSharedPreferenceChangeListener(this);
		}
		super.onPause();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Activity activity = getActivity();
		boolean enable = sharedPreferences.getBoolean(
				SettingsActivity.PREF_ENABLE,
				getResources().getBoolean(R.bool.pref_enable_default));

		switch (key) {
		case SettingsActivity.PREF_ENABLE:
			PreferenceScreen screen = getPreferenceScreen();
			for (int i = 0, ii = screen.getPreferenceCount(); i < ii; i++) {
				Preference pref = screen.getPreference(i);
				if (!"pref_enable".equals(pref.getKey())) {
					pref.setEnabled(enable);
				}
			}
			break;
		case SettingsActivity.PREF_TIMEOUT:
			ListPreference pref = (ListPreference) findPreference(key);
			String timeoutString = sharedPreferences.getString(key,
					getResources().getString(R.string.pref_timeout_default));
			int timeout = Integer.parseInt(timeoutString);
			String delayText;
			if (timeout < 60) {
				delayText = timeoutString + " seconds";
			} else if (timeout == 60) {
				delayText = "1 minute";
			} else {
				delayText = (timeout / 60) + " minutes";
			}
			pref.setSummary("Restore volume after " + delayText);
			break;
		case SettingsActivity.PREF_VOLUME_LEVEL_REL:
			AudioManager audioManager = (AudioManager) activity
					.getSystemService(Context.AUDIO_SERVICE);
			int maxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			int relVolume = sharedPreferences.getInt(key, getResources()
					.getInteger(R.integer.pref_volume_level_default));
			int volume = (int) Math.floor(maxVolume * relVolume / 100.0);

			// update absolute volume level
			Editor editor = sharedPreferences.edit();
			editor.putInt(SettingsActivity.PREF_VOLUME_LEVEL, volume);
			editor.apply();
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume,
					AudioManager.FLAG_PLAY_SOUND);
			break;
		}

		// stop (and restart if enabled) service after changing settings
		Intent intent = new Intent(activity, BackgroundService.class);
		activity.stopService(intent);
		if (enable) {
			activity.startService(intent);
		}
	}
}