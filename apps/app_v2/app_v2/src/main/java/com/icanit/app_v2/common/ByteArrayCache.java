package com.icanit.app_v2.common;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import android.util.Log;

public class ByteArrayCache {
	private static ByteArrayCache byteArrayCache;
	public LinkedHashMap<String,byte[]> cache=new LinkedHashMap<String,byte[]>(10,1.5f,true);
	private long size=0;
	public long limitBytes=Runtime.getRuntime().maxMemory()/10;
	private ByteArrayCache(){}
	
	public static ByteArrayCache getInstance(){
		if(byteArrayCache==null)
			synchronized(ByteArrayCache.class){
				if(byteArrayCache==null)  byteArrayCache=new ByteArrayCache();
			}
		return byteArrayCache;
	}
	
	public byte[] getByteArray(String url){
		Log.d("byteArrayCache","getfromByteArrayCache   size="+size+",limitBytes="+limitBytes+"  @byteArrayCache");
		return cache.get(url);
	}
	public void cacheByteArray(String url,byte[] ary){
		cache.put(url, ary);
		Log.d("byteArrayCache","beforeCache   size="+size+",limitBytes="+limitBytes+"  @byteArrayCache");
		size+=ary.length;
		Log.d("byteArrayCache","afterCache   size="+size+",limitBytes="+limitBytes+"  @byteArrayCache");
		checkSize();
	}
	private void checkSize(){
		if(size>limitBytes){
			Log.d("byteArrayCache","beforeClean   size="+size+",limitBytes="+limitBytes+"  @byteArrayCache");
			Iterator<Entry<String,byte[]>> iterator=cache.entrySet().iterator();
			while(iterator.hasNext()){
				if(size<limitBytes*2/3) break;
				Entry<String,byte[]> en=iterator.next();
				size-=en.getValue().length;
				iterator.remove();
			}
			Log.d("byteArrayCache","afterClean   size="+size+",limitBytes="+limitBytes+"  @byteArrayCache");
		}
	}
	
	public void clear(){
		cache.clear();
		size=0;
	}
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clear();
	}
}
