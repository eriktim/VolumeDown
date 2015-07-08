package nl.gingerik.volumedown;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	public final static String PREF_ENABLE = "pref_enable";
	public final static String PREF_LOG = "pref_log";
	public final static String PREF_TIMEOUT = "pref_timeout";
	public final static String PREF_TOAST = "pref_toast";
	public final static String PREF_VOLUME_LEVEL = "pref_volume_level";
	public final static String PREF_VOLUME_LEVEL_REL = "pref_volume_level_rel";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
		setTitle(R.string.settings_activity_title);
	}
}
