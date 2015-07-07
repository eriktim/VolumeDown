package nl.gingerik.volumedown;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		// Set the maximum volume
		AudioManager audioManager = (AudioManager) getActivity()
				.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		SeekBarPreference seekBar = (SeekBarPreference) findPreference(SettingsActivity.PREF_VOLUME_LEVEL);
		seekBar.setMax(maxVolume);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		switch (key) {
		case SettingsActivity.PREF_TIMEOUT:
			ListPreference pref = (ListPreference) findPreference(key);
			int timeout = sharedPreferences.getInt(key,
					R.integer.pref_timeout_default);
			String delayText;
			if (timeout < 60) {
				delayText = timeout + " seconds";
			} else if (timeout == 60) {
				delayText = "1 minute";
			} else {
				delayText = (timeout / 60) + " minutes";
			}
			pref.setSummary("Restore volume after " + delayText);
		}
	}
}