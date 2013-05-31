package com.tiankonguse.gameplatform.listener;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.nfc.tech.IsoDep;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.tiankonguse.gameplatform.manger.Install;
import com.tiankonguse.gameplatform.net.FileUtils;
import com.tiankonguse.gameplatform.net.SystemDownload;

public class InstallListener implements OnClickListener{
	String id;
	String appname;
	String apk;
	Context context;
	String SDName ;
	
	public  InstallListener() {
		
	}
	
	public InstallListener(String id, String appname, Context context,String apk) {
		super();
		this.id = id;
		this.appname = appname;
		this.context = context;
		this.apk = apk;
		this.SDName = id + ".apk";
		
	}

	@Override
	public void onClick(View v) {
		if(FileUtils.isFileExist("tiankonguse/game/apk/",id+".apk")){
			 Toast.makeText(context,
			appname + "游戏已下载，现在将进行安装",
			 Toast.LENGTH_SHORT).show();
			 String apkFilePath = new StringBuilder(FileUtils.getPath("tiankonguse/game/apk/", id + ".apk")).toString();
			 Install.install(context, apkFilePath);
		}else{
			Toast.makeText(context,
					appname + "已加入下载队列",
					 Toast.LENGTH_SHORT).show();
			SystemDownload systemDownload = new SystemDownload(apk,"tiankonguse/game/apk/",id,context,appname);
			systemDownload.download();
			Log.i("InstallListener", id);
			CompleteReceiver completeReceiver = new CompleteReceiver(id,systemDownload.getDownloadId(),systemDownload.getDownloadManager());
			/** register download success broadcast **/
			context.registerReceiver(completeReceiver, new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		}
	}
}
