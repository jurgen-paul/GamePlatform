package com.tiankonguse.gameplatform.net;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

public class SystemDownload {

	String apkUrl;
	String path;
	String id;
	String appname;
	Context context;
	String SDName ;
	long downloadId;
	
	public SystemDownload() {
		
	}
	
	
	public SystemDownload(String apkUrl, String path, String id, Context context, String name) {
		super();
		this.apkUrl = apkUrl;
		
		this.id = id;
		this.context = context;
		this.appname = name;
		this.path = path;
		this.SDName = id + ".apk";
		FileUtils.createSDDir(path);
	}

	public void download() {
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(apkUrl));
		request.setDestinationInExternalPublicDir(path, SDName);
		request.setTitle("tiankong游戏平台");
		request.setDescription("正在下载游戏:" + appname);
		request.setMimeType("application/com.tiankonguse.gameplatform.manger.Install");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		downloadId = downloadManager.enqueue(request);
	}

}
