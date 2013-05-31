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
	public static boolean unstall(Context context, String packageName) {
		Uri uri = Uri.fromParts("package", packageName, null);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(intent);
		return true;
	}
}

