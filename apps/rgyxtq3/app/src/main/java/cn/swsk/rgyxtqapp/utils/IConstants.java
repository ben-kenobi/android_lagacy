package cn.swsk.rgyxtqapp.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public interface IConstants {
	public static final ExecutorService THREAD_POOL=Executors.newFixedThreadPool(3);
	public static final Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());

//	String SERVER="s192.168.0.171:8080/";
//	String SERVER="http://61.154.9.242:5551/";

			
	
	
}