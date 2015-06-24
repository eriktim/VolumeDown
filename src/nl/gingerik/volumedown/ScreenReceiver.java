package nl.gingerik.volumedown;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
	
	private final Logger mLog;
	private PendingIntent mPendingIntent;
	private int mDelay;
	
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
    	        mLog.v("Cancelling alarm");
        		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    			alarmManager.cancel(mPendingIntent);
    			mPendingIntent = null;
    		}
    		break;
    	case Intent.ACTION_SCREEN_OFF:
    		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    		Intent receiverIntent = new Intent(context, CountdownReceiver.class);
    		mPendingIntent = PendingIntent.getBroadcast(context, 0,  receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    		alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + mDelay, mPendingIntent);
    		break;
        }
	}

}
