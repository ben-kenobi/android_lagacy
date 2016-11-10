package com.icanit.bdmapversion2.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;

public class CommonUtil {
	
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}
	
	public static String getRootFilePath() {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/";// filePath:/sdcard/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath: /data/data/
		}
	}
	
	
	public static boolean isNetworkAvailable(Activity mActivity){  
        Context context = mActivity.getApplicationContext();  
          
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(  
                Context.CONNECTIVITY_SERVICE);  
          
        if(connectivity == null){  
            return false;  
        }else {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if(info != null ){  
                for(int i=0; i<info.length; i++){  
                    if(info[i].getState() == NetworkInfo.State.CONNECTED){  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }  
      
    public static boolean checkhttp(final Activity activity){  
        if(!isNetworkAvailable(activity)){  
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);  
            builder.setTitle("网络错误，请检查网络设置");  
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
                  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                   Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");  
                   activity.startActivity(intent);  
                   activity.finish();  
                }  
            });  
            builder.show();
            return false;
        }else {
        	return true;
        }
    } 

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}
	
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}
	
}
