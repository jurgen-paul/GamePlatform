package com.tiankonguse.gameplatform.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public final class DownLoadList {
	
	private String url = "";
	
	private String text = "";
	
	private static int ch = -1;
	
	private static byte[] buf = new byte[1024*1024];
	
	public DownLoadList(String url) {
		this.url += url;
	}

	public DownLoadList Begin() {
		try {
			HttpClient client = new DefaultHttpClient();

			HttpGet get = new HttpGet(this.url);
			
			HttpResponse response = client.execute(get);
			
			HttpEntity entity = response.getEntity();
						
			InputStream is = entity.getContent();
						
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while ((ch = is.read(buf)) != -1) {
				baos.write(buf, 0, ch);
				Thread.sleep(100);
			}
			
			text = new String(baos.toByteArray());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public String getText() {
		return this.text;
	}
	
}
