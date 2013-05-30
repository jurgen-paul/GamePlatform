package com.tiankonguse.gameplatform.net;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadFile {
	private int fileSize;
	private int downLoadFileSize;
	private Handler handler;
	private FileOutputStream fos;

	public DownloadFile(Handler handler) {
		this.handler = handler;
	}

	public DownloadFile() {
		this.handler = null;
	}

	public void down_file(String url, String path, String name) throws IOException {
		Log.i("DownloadFile", url + " " + path + "/" + name);
		FileUtils.createSDDir(path);
		URL myURL = new URL(url);
		URLConnection conn = myURL.openConnection();
		conn.connect();
		InputStream is = conn.getInputStream();
		this.fileSize = conn.getContentLength();
		if (this.fileSize <= 0) {
			throw new RuntimeException("filesize too big");
		}
		if (is == null) {
			throw new RuntimeException("stream is null");
		}
		fos = new FileOutputStream(FileUtils.getPath(path, name));
		
		byte buf[] = new byte[1024];
		downLoadFileSize = 0;

		sendMsg(0);

		do {
			int numread = is.read(buf);
			if (numread == -1) {
				break;
			}
			fos.write(buf, 0, numread);
			downLoadFileSize += numread;
			sendMsg(1);
		} while (true);
		sendMsg(2);

		try {
			is.close();
		} catch (Exception ex) {
			Log.e("DownloadFile", "error: " + ex.getMessage(), ex);
		}
	}

	private void sendMsg(int flag) {
		if (this.handler != null) {
			Message msg = new Message();
			msg.what = flag;
			handler.sendMessage(msg);
		}

	}
}
