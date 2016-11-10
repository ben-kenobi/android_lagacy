package com.icanit.app_v2.common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.graphics.Bitmap;
import android.util.Log;

public class MemoryCache implements Runnable{
	private static MemoryCache cache;
	private FileCache fileCache=FileCache.getInstance();
	public LinkedHashMap<String,Bitmap> bitmapCache=new LinkedHashMap<String,Bitmap>(10,1.5f,true);
	public long limitBytes=Runtime.getRuntime().maxMemory()/5;
	public long size=0;
	public static MemoryCache getInstance(){
		if(cache==null)
			synchronized(MemoryCache.class){
				if(cache==null)  cache=new MemoryCache();
			}
		return cache;
	}
	private MemoryCache(){
	}
	public Bitmap getBitmap(String url){
		return bitmapCache.get(url);
	}
	public synchronized void cacheBitmap(String url,Bitmap bitmap){
		Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  preCacheBitmap");
		if(bitmapCache.containsKey(url)) return;
		size+=getBytes(bitmap);
		bitmapCache.put(url, bitmap);
		Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  anaCacheBitmap");
		checkSize();
	}
	public  void clear(){
		bitmapCache.clear();
	}
	
	private void checkSize(){
		if(size>limitBytes){
			IConstants.THREAD_POOL.submit(this);
		}
	}
	
	public void run() {
		Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  beforeCheckSize");
		Iterator<Entry<String,Bitmap>> iterator = bitmapCache.entrySet().iterator();
		while(iterator.hasNext()){
			Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  cycleHeadCheckSize");
			if(size<limitBytes/3*2) break;
			Entry<String,Bitmap> en=iterator.next();
			size-=getBytes(en.getValue());
			iterator.remove();
			if(fileCache!=null)
			try {
				fileCache.cacheBitmap(en.getKey(), en.getValue());
				Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  cycleEndCheckSize");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.d("imageInfo","size="+size+",limitBytes="+limitBytes+" @MemoryCache  afterCheckSize");
		
	}
	private long getBytes(Bitmap bitmap){
		if(bitmap==null) return 0;
		return bitmap.getRowBytes()*bitmap.getHeight();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
	
}
