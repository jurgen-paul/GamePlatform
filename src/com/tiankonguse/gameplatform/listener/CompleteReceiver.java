package com.tiankonguse.gameplatform.listener;

import java.io.File;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.tiankonguse.gameplatform.R.id;
import com.tiankonguse.gameplatform.manger.Install;
import com.tiankonguse.gameplatform.net.DownloadManagerPro;
import com.tiankonguse.gameplatform.net.FileUtils;



class CompleteReceiver extends BroadcastReceiver {
	
	long downloadId;
	String id;
	DownloadManagerPro downloadManagerPro;
	
	private static final String PATH_STRING = "tiankonguse/game/apk/";
	
	public CompleteReceiver(){
		
	}
	
	public CompleteReceiver(String id,long downloadId, DownloadManager downloadManager){
		this.id = id;
		this.downloadId = downloadId;
		this.downloadManagerPro = new DownloadManagerPro(downloadManager);
	}
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * get the id of download which have download success, if the id is
		 * my id and it's status is successful, then install it
		 **/
		long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
		
		if (completeDownloadId == downloadId) {
			Log.i("CompleteReceiver", downloadId + "" + completeDownloadId);
			if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
				String apkFilePath = new StringBuilder(FileUtils.getPath(PATH_STRING, id + ".apk")).toString();;
				Log.i("CompleteReceiver","-->");
				Install.install(context, apkFilePath);
			}
		}
		
	}

}



