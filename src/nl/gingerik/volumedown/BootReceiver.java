package nl.gingerik.volumedown;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	
	private final Logger mLog = new Logger(BootReceiver.class.getSimpleName());

	@Override
	public void onReceive(Context context, Intent intent) {
        mLog.v("Received intent");
        Intent serviceIntent = new Intent(context, VolumeControlService.class);
        context.startService(serviceIntent);
	}

}
