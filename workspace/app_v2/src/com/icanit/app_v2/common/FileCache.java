package com.icanit.app_v2.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.icanit.app_v2.util.DeviceInfoUtil;

public class FileCache {
	private static FileCache cache;
	private long maxSize;
	private long maxSpace=512*1024*1024;
	public static  final String tempDirPath=Environment.getExternalStorageDirectory().getPath()+File.separator+"_appImgTemp";
	private LinkedList<String> list= new LinkedList<String>();
	public static FileCache getInstance(){
		if(!DeviceInfoUtil.isSDCardMounted())return null;
		if(cache==null)
		synchronized (FileCache.class) {
			if(cache==null) 	cache=new FileCache();
		}
		return cache;
	}
	private FileCache(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){ 
			maxSize=Environment.getExternalStorageDirectory().getUsableSpace()/3;
			if(maxSize>maxSpace) maxSize=maxSpace;
		}else{
			maxSize=0;
		}
		File dir = new File(tempDirPath);
		if(dir.exists()) dir.delete();
		boolean b = dir.mkdir();
		Log.d("fileInfo","temDirPath="+tempDirPath+",size="+maxSize+",dirIsMade="+b+"  @FileCache");
	}
	
	public void  cacheBitmap(String url,Bitmap bitmap) throws Exception{
		FileOutputStream fos=null;
		try {
			url=transformUrl(url);
			String urlPrefix=url.substring(0,url.lastIndexOf(File.separator));
			File dir = new File(tempDirPath+File.separator+urlPrefix);
			if(!dir.exists()) dir.mkdirs();
			File file = new File(tempDirPath+File.separator+url);
			if(file.exists())return;
			fos= new FileOutputStream(file);
			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100,fos);
			Log.d("imageInf","compressSuccess="+b+"  @FileCache");
			fos.flush();
			list.add(url);
//			bitmap.recycle();
		}finally{
			if(fos!=null)
				fos.close();
		}
		checkSize();
	}
	private String transformUrl(String url){
		return url.substring(7,url.lastIndexOf(".")==-1?url.length():url.lastIndexOf(".")).
				replaceAll(":", File.separator);
	}
	public Bitmap getBitmap(String url) throws Exception{
		url=transformUrl(url);
		File file = new File(tempDirPath+File.separator+url);
		if(file.exists()) { 
			Bitmap bitmap= decodeFile(file);
			file.delete();
			list.remove(url);
			return bitmap;
		}
		return null;
	}
	private Bitmap decodeFile(File f) throws IOException{
		InputStream is1=null,is2=null;
		try{
			BitmapFactory.Options o=new BitmapFactory.Options();
			o.inJustDecodeBounds=true;
//			is1=new FileInputStream(f);
			Bitmap bm1=BitmapFactory.decodeFile(f.getPath(), o);
			o.inJustDecodeBounds=false;o.inPreferredConfig=Bitmap.Config.ARGB_4444;
//			int sampleSize=o.outWidth/(DeviceInfoUtil.getScreenWidth()-40);
//			Log.i("bmInfo","bitmap1="+bm1+",outWidth="+o.outWidth+",sampleSize="+sampleSize+"  @FileCache");
//			o.inSampleSize=sampleSize;
//			is2=new FileInputStream(f);
			Bitmap bm2= BitmapFactory.decodeFile(f.getPath(), o);
			Log.i("bmInfo","bitmap2="+bm2+",outWidth="+o.outWidth+",filePath="+f.getPath()+"  @FileCache");
			return bm2;
		}finally{
			if(is1!=null)is1.close();
			if(is2!=null)is2.close();
		}
	}
	private void checkSize(){
		File file = new File(tempDirPath);
		Log.d("fileInfo","beforeCheckTempDirSize="+getDirBytes(file)+"  @FileCache");
		if(getDirBytes(file)<maxSize) return;
		for(int i= 0;i<list.size();i++){
			String url = list.removeFirst();
			File file1 = new File(tempDirPath+File.separator+url);
			if(file1.exists()) file1.delete();
			Log.d("fileInfo","betweenCheckTempDirSize="+getDirBytes(file)+"  @FileCache");
			if(getDirBytes(file)<maxSize/2) break;
		}
		Log.d("fileInfo","afterCheckTempDirSize="+getDirBytes(file)+"  @FileCache");
	}
	
	public static void clear(){
		deleteFileRecursively(new File(tempDirPath));
	}
	
	private static void deleteFileRecursively(File file){
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++){
				deleteFileRecursively(files[i]);
			}
		}
		file.delete();
	}
	private long getDirBytes(File file){
		long size=0;
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++){
				size+=getDirBytes(files[i]);
			}
		}else{
			size=file.length();
		}
		return size;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
}
