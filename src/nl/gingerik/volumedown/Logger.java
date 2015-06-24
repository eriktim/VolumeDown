package nl.gingerik.volumedown;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

public class Logger {

	private final SimpleDateFormat mFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm'Z'", Locale.US);
	private String mTag;
	private File mFile;

	public Logger(Context context, String tag) {
		mTag = tag;
		File externalStoragePath = context.getExternalFilesDir(null);

		mFile = new File(externalStoragePath.getPath() + "/volumedown.log");
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