package com.tiankonguse.gameplatform.manger;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Unstall {
	/**
	 * install app
	 * 
	 * @param context
	 * @param filePath
	 * @return whether apk exist
	 */
	public static boolean unstall(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath),
					"application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}
}

