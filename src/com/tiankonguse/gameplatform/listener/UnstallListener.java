package com.tiankonguse.gameplatform.listener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.tiankonguse.gameplatform.manger.Unstall;

public class UnstallListener implements OnClickListener{
	String packageName;
	Context context;
	
	public UnstallListener(){
		
	}
	
	public UnstallListener(String packageName, Context context){
		this.context = context;
		this.packageName = packageName;
	}
	
	@Override
	public void onClick(View v) {
		Unstall.unstall(context, packageName);	
		
	}

}
