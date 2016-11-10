package com.what.yunbao.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

public class SharedPrefrencesUtil {
 	private static final String TAG = "SharedPrefrencesUitl";
    public static final String HISTORY_SHAREDPREFERENCE_NAME = "PRE_HOSTORY";
    public static final String PUSH_SHAREDPREFERENCE_NAME = "PRE_PUSH";
    public static final String PUSH_PREFS_DAYS = "PUSH_DAYS";
    public static final String PUSH_PREFS_START_TIME = "PUSH_START_TIME";
    public static final String PUSH_PREFS_END_TIME = "PUSH_END_TIME";
    
 	public static void saveObjectPre(String per_name,Object vaule,long businessId,Context context){  
		Log.e(TAG, "商户id "+businessId);
        SharedPreferences historyInfo = context.getSharedPreferences(per_name, Context.MODE_APPEND);  
        Editor preEd = null;  
        if( historyInfo != null){  
            preEd =  historyInfo.edit();
            if(historyInfo.getString("history"+businessId, "")!=""){
            	Log.e(TAG, "Aleady exist!");
            	return;
            }           	          
        }  
        if(preEd != null){  
        	
            if(vaule != null){  
            	
                ByteArrayOutputStream baos = new ByteArrayOutputStream();  
                ObjectOutputStream oos=null;  
                try {  
                 oos = new ObjectOutputStream(baos);  
                 oos.writeObject(vaule);  
                } catch (IOException e) {  
                 e.printStackTrace();  
                }  
                String info = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);	
                
//	                preEd.putInt("count", 1);
                preEd.putString("history"+businessId, info); 
            }  
            preEd.commit();  
        }  
	 } 
	 
	 
	 public static <T> List<T> readAllObjectPre(String per_name,Context context){  
         SharedPreferences historyInfo = context.getSharedPreferences(per_name, Context.MODE_APPEND);  
         Map<String, ?> maps = historyInfo.getAll();//取出所有数据  
         
//	         String info =historyInfo.getString("business", "none");//取单条数据
         List<T> oblist = new ArrayList<T>();  
         Iterator<?> it = maps.values().iterator();  
         while(it.hasNext()){  
            String base64Str = (String)it.next(); 
            byte[] base64Bytes = Base64.decode(base64Str.getBytes(),Base64.DEFAULT);  
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
            ObjectInputStream ois;  
            try {  
              ois = new ObjectInputStream(bais);  
              T tempobj = (T) ois.readObject();  
              oblist.add(tempobj);  
            } catch (StreamCorruptedException e) {  
              e.printStackTrace();  
            } catch (IOException e) {  
              e.printStackTrace();  
            } catch (ClassNotFoundException e) {  
              e.printStackTrace();  
            }  
         }  
         Log.e(TAG,"长度  "+oblist.size());
         return oblist;  
   }  
}
