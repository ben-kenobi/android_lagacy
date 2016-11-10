package com.icanit.bdmapversion2.activity;

import java.io.File;
import java.util.Arrays;

import com.icanit.bdmapversion2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class LoadingActivity extends Activity{ 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		new AsyncTask<String, Integer, String>() {

			@Override 
			protected String doInBackground(String... params) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}		
				
				
//				File file = new File("/data/data/com.icanit.bdmapversion2/cache");
//		        if(file.exists()){
//		        
//		        if(file.isFile()){
//		        	Log.e("???????", file.length()+"");
//		        }else{
//		        	Log.e("???ddddddd????", Arrays.toString(file.list()));
//		        	int a =0;
//		        	for(int i=0;i<file.listFiles().length;i++){
//		        		a+=file.listFiles()[i].length();
//		        	}
//		        	Log.e("sssssssssss", a+"");
//		        }
//		    	 
//		        } 
				
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				Intent intent=new Intent();
//				intent.setClassName(LoadingActivity.this, getString(R.string.));
				intent.setClassName(LoadingActivity.this, "com.icanit.bdmapversion2.activity.LocationActivity");
				intent.putExtra("versionState", result);
				startActivity(intent);
//				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				finish();
			}
		}.execute();
	}
}
