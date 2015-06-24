package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;

public class ScreenReceiver extends BroadcastReceiver {
	
	private final Logger mLog;
	private final Handler mHandler = new Handler();
	private Context mContext;
	
	public ScreenReceiver(Context context) {
        mLog = new Logger(context, ScreenReceiver.class.getSimpleName());
        mLog.v("Create ScreenReceiver");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
        mLog.v("Received " + intent.getAction());
        mContext = context;
        
        switch (intent.getAction()) {
    	case Intent.ACTION_SCREEN_ON:
    		mHandler.removeCallbacks(mTask);
    		break;
    	case Intent.ACTION_SCREEN_OFF:
    		mHandler.postDelayed(mTask, 30000);
    		break;
        }
	}
	
	private Runnable mTask = new Runnable() {
		@Override
		public void run() {
			int volume = 0;
	        mLog.v("Reset volume to " + volume);
	        AudioManager audioManager = (AudioManager)
	        		mContext.getSystemService(Context.AUDIO_SERVICE);
	        audioManager.setStreamVolume(
	        		AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
		}
	};

}
