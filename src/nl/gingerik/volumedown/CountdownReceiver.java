package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class CountdownReceiver extends BroadcastReceiver {

	private Logger mLog;
	private int mVolume;

	@Override
	public void onReceive(Context context, Intent intent) {
		mLog = new Logger(context, CountdownReceiver.class.getSimpleName());
		mLog.v("Received " + intent.getAction());

		mVolume = 0; // FIXME get from settings
		mLog.i("Reset volume to " + mVolume);

		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume,
				AudioManager.FLAG_SHOW_UI);
	}

}
