package com.tiankonguse.gameplatform.net;

import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  

import javax.xml.datatype.Duration;
  
import android.os.Environment;  
import android.util.Log;
  
public class FileUtils {  
    private static String SDPATH = Environment.getExternalStorageDirectory() + "/";  
      
    private  static int FILESIZE = 4 * 1024;   
      
    public  static String getSDPATH(){  
        return SDPATH;  
    }  
      
    public FileUtils(){  
        //得到当前外部存储设备的目录( /SDCARD )  
//        SDPATH = Environment.getExternalStorageDirectory() + "/";  
    }  
      
    /**  
     * 在SD卡上创建文件  
     * @param fileName  
     * @return  
     * @throws IOException  
     */  
    public  static File createSDFile(String fileName) throws IOException{  
        File file = new File(SDPATH + fileName);  
        file.createNewFile();  
        return file;  
    }  
    
    public static String getPath(String path, String fileName){
    	return SDPATH + path + fileName;
    }
    public static String getPath(String path){
    	return SDPATH + path;
    }
    
      
    /**  
     * 在SD卡上创建目录  
     * @param dirName  
     * @return  
     */  
    public static File createSDDir(String dirName){  
        
        
    	File dir = null;
    	
    	String[] dirpath = dirName.split("/");
    	dirName = "";
    	
    	for (int i = 0; i < dirpath.length; i++) {
    		dirName += "/" + dirpath[i];
    		dir = new File(SDPATH + dirName); 
    		if(!dir.exists()){
    			dir.mkdir(); 
    		}
    		Log.i("warn", "SDPATH"+dirName);
		}

        return dir;  
    }  
      
    /**  
     * 判断SD卡上的文件夹是否存在  
     * @param fileName  
     * @return  
     */  
    public static boolean isFileExist(String fileName){  
        File file = new File(SDPATH + fileName);  
        return file.exists();  
    }  
    public static boolean isFileExist(String path, String fileName){  
        File file = new File(SDPATH + path +  fileName);  
        return file.exists();  
    }  
      
    /**  
     * 将一个InputStream里面的数据写入到SD卡中  
     * @param path  
     * @param fileName  
     * @param input  
     * @return  
     */  
    public  static File write2SDFromInput(String path,String fileName,InputStream input){  
        File file = null;  
        OutputStream output = null;  
        
        try {  
            createSDDir(path);  
            file = createSDFile(path + fileName);  
            output = new FileOutputStream(file);  
            byte[] buffer = new byte[FILESIZE];  
            while((input.read(buffer)) != -1){  
                output.write(buffer);  
            }  
            output.flush();  
        }   
        catch (Exception e) {  
            e.printStackTrace();  
        }  
        finally{  
            try {  
                output.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return file;  
    }  
  
}  