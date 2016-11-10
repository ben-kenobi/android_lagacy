package fj.swsk.cn.eqapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DeviceInfoUtil {
	
	
	public static Point getScreenSize() {
		Point point = new Point();
		((WindowManager)CommonUtils.context.getSystemService(Context.WINDOW_SERVICE))
		.getDefaultDisplay().getSize(point);
		return point;
	}


	public static Point getScreenSize2(){
		Point point = new Point();
		DisplayMetrics dis=getDisplayMetrics();
		point.set(dis.widthPixels, dis.heightPixels);
		return point;
	}
	public static float getScreenDensity(){
		Point point = new Point();
		DisplayMetrics dis=getDisplayMetrics();
		return dis.density;
	}
	public static float getScreenDp(){
		Point point = new Point();
		DisplayMetrics dis=getDisplayMetrics();
		return dis.densityDpi;
	}

	public static DisplayMetrics getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)CommonUtils.context.getSystemService(Context.WINDOW_SERVICE))
		.getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	public static int getScreenWidth() {
		return getDisplayMetrics().widthPixels;
	}
	public static int getScreenHeight() {
		return getDisplayMetrics().heightPixels;
	}
	
	private static void printLogs(ConnectivityManager connMan){
		for(NetworkInfo info:connMan.getAllNetworkInfo()){
			System.out.println(info.getExtraInfo()+" | typeName="+info.getTypeName()+" | available="+
		info.isAvailable()+" | connection="+info.isConnected()+"   @DeviceInfoUtil");
		}
	}
	private static void printLogs(NetworkInfo info){
		if(info!=null)
			System.out.println(info.getExtraInfo()+" | typeName="+info.getTypeName()+" | available="+
		info.isAvailable()+" | connection="+info.isConnected()+"   @DeviceInfoUtil");
	}
	public static boolean isNetworkConnected(){
		ConnectivityManager connMan = (ConnectivityManager)CommonUtils.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMan.getActiveNetworkInfo();
		printLogs(info);
		if(info!=null) return info.isConnected();
		return false;
	}
	public static boolean isNetworkAvailable(){
		ConnectivityManager connMan = (ConnectivityManager)CommonUtils.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMan.getActiveNetworkInfo();
		printLogs(info);
		if(info!=null) return info.isAvailable();
		return false;
	}
	
	public static boolean isSDCardMounted(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
    public static void checkNetwork(final Context context){  
        if(!isNetworkAvailable()){  
            CustomizedDialog.Builder builder = new CustomizedDialog.Builder(context);
            builder.setTitle("network malfunction!");
            builder.setMessage("网络不可用,设置？");  
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int which) {  
                   Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");  
                   context.startActivity(intent);  
                }  
            });  
            builder.setNegativeButton("放弃", null);
            builder.create().show(); 
        }  
    } 
	
	
}
