package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	private Logger mLog;

	@Override
	public void onReceive(Context context, Intent intent) {
		mLog = new Logger(context, BootReceiver.class.getSimpleName());
		mLog.v("Received " + intent.getAction());
		Intent serviceIntent = new Intent(context, BackgroundService.class);
		context.startService(serviceIntent);
	}

}
