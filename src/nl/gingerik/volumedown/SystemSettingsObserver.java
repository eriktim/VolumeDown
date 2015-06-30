package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.widget.Toast;

public class SystemSettingsObserver extends ContentObserver {

	private final Logger mLog;
	private BroadcastReceiver mScreenReceiver;
	private Context mContext;
	private int mVolume;

	public SystemSettingsObserver(Context context, Handler handler) {
		super(handler);

		mLog = new Logger(context, SystemSettingsObserver.class.getSimpleName());
		mLog.v("Create SettingsContentObserver");
		mContext = context;
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mVolume = 0; // FIXME get from settings

		if (volume != mVolume) {
			onChange(true);
		}
	}

	@Override
	public boolean deliverSelfNotifications() {
		return super.deliverSelfNotifications();
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);

		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		if (mScreenReceiver != null) {
			mContext.unregisterReceiver(mScreenReceiver);
			mScreenReceiver = null;
		}

		if (volume != mVolume) {
			mScreenReceiver = new ScreenStateReceiver(mContext);

			mLog.v("Settings change detected");
			Toast.makeText(mContext, "Detected volume change",
					Toast.LENGTH_SHORT).show();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			mContext.registerReceiver(mScreenReceiver, filter);
		}
	}
}
