package nl.gingerik.volumedown;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Logger {

	private final SimpleDateFormat mFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
	private Context mContext;
	private String mTag;
	private File mFile;
	private boolean mLogToFile;

	public Logger(Context context, String tag) {
		mContext = context;
		mTag = tag;
		

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		mLogToFile = sharedPref.getBoolean(SettingsActivity.PREF_LOG, false);
		if (mLogToFile) {
			setLogFile();
		}
	}

	private boolean setLogFile() {
		File externalStoragePath = mContext.getExternalFilesDir(null);
		if (externalStoragePath == null) {
			Log.w(mTag, "External storage is not ready. "
					+ "Not writing to log file.");
			return false;
		}
		mFile = new File(externalStoragePath.getPath() + "/volumedown.log");
		return true;
	}

	public void v(String message) {
		Log.v(mTag, message);
		appendLog("VERBOSE", message);
	}

	public void d(String message) {
		Log.d(mTag, message);
		appendLog("DEBUG  ", message);
	}

	public void i(String message) {
		Log.i(mTag, message);
		appendLog("INFO   ", message);
	}

	public void e(String message) {
		Log.e(mTag, message);
		appendLog("ERROR  ", message);
	}

	private void appendLog(String level, String message) {
		if (!mLogToFile) {
			return;
		}
		if (mFile == null) {
			setLogFile();
			if (mFile == null) {
				return;
			}
		}
		if (!mFile.exists()) {
			try {
				mFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(mFile, true));
			buf.append(mFormat.format(new Date()));
			buf.append("\t" + level);
			buf.append("\t" + mTag);
			buf.append("\t" + message);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}