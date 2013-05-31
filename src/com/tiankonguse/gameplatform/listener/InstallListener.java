package com.tiankonguse.gameplatform.listener;

import com.tiankonguse.gameplatform.net.FileUtils;
import com.tiankonguse.gameplatform.net.SystemDownload;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
			appname + "游戏已下载，是否安装",
			 Toast.LENGTH_SHORT).show();
		}else{
			SystemDownload systemDownload = new SystemDownload(apk,"tiankonguse/game/apk/",id,context,appname);
			systemDownload.download();
		}
	}
}
