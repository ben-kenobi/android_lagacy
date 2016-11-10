package com.what.yunbao.util;

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
import android.widget.Toast;

public class CommonUtil {
	
	
	/**
	 *是否有sdcard
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		} 
		return true;
	}
	/**
	 * 根据sdcard获取路径
	 * @return
	 */
	public static String getRootFilePath(Context context) {
		if (hasSDCard()) {
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/"
		+context.getPackageName()+"/";// filePath:/sdcard/android/data/packagename/
		} else {
			return Environment.getDataDirectory().getAbsolutePath() + "/data/"
		+context.getPackageName()+"/"; // filePath: /data/data/packagename/
		}
	}
	
	/**
	 * 网络是否可用
	 * @param activity
	 */
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
      
	/**
	 * 网络是否可用-设置网络
	 * @param activity
	 */
    public static void checkhttp(final Activity activity){  
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
        }  
    } 

	public static void showToast(String text,Toast mToast) {  		 
//    	mToast.setGravity(Gravity.CENTER, 0, 110);
        mToast.setText(text);            
        mToast.setDuration(Toast.LENGTH_SHORT);  
        mToast.show();         
    }  
	
	/**
	 * 获取屏幕宽高
	 * @param context
	 * @return
	 */
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
	
	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3)
					+ "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3)
					+ "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3)
					+ "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}
	
}
