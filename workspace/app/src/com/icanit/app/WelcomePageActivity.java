package com.icanit.app;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.service.DataService;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.DeviceInfoUtil;
import com.icanit.app.util.SharedPreferencesUtil;

public class WelcomePageActivity extends Activity{
	private ImageView imageView;
	private ProgressBar pb;
	private final int dotCycleCount=12;
	private AnimationDrawable ad;
	private String newVersionName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcompage);
		init();pbSetUp();/*checkVersion();*/
		checkAutoLoginIf();
		overridePendingTransition(R.anim.alpha_fade_in, R.anim.alpha_fade_out);
		Log.d("deviceInfo","screen="+DeviceInfoUtil.getScreenSize()+",sdcard="+DeviceInfoUtil.isSDCardMounted()+",\n" +
				"network="+DeviceInfoUtil.isNetworkAvailable()+":"+DeviceInfoUtil.isNetworkConnected());
	}
	private void init(){
		imageView=(ImageView)findViewById(R.id.imageView1);
		pb=(ProgressBar)findViewById(R.id.progressBar1);
		ad=(AnimationDrawable)imageView.getBackground();
		ad.start();
	}
	private void pbSetUp(){
		pb.setMax(5);
		DataService.THREAD_POOL.submit(new Runnable(){
			public void run(){
				for(int i=0;i<dotCycleCount;i++){
					pb.setProgress(i%5+1);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				ad.stop();
				Intent intent=new Intent(WelcomePageActivity.this,HomePageActivity.class);
				startActivity(intent);
				WelcomePageActivity.this.finish();
			}
		});
	}
	
	private void checkAutoLoginIf(){
		SharedPreferencesUtil share = AppUtil.getSharedPreferencesUtilInstance();
		String str=share.get(null,IConstants.AUTO_LOGIN);
		if(str!=null&&"true".equalsIgnoreCase(str)){
			final String phone=share.get(null, IConstants.PHONE);
			final String password=share.get(null,IConstants.PASSWORD);
			if(phone==null||password==null) return;
			DataService.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						AppUtil.loginOnClient(phone, password,AppUtil.loginOnServer(phone, password));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private void checkVersion(){
		AsyncTask<Void,Void,String> at = new AsyncTask<Void, Void, String>(){
			protected String doInBackground(Void... params) {
				try {
					return AppUtil.getNetUtilInstance().
							sendMessageWithHttpGet(UriConstants.GET_LATEST_APP_VERSION,"");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			protected void onPostExecute(String result) {
				if(result==null||"".equals(result)) {
					//timeUpdater.startTimer();	return;
					//1  response fail
					Intent intent=new Intent(WelcomePageActivity.this,HomePageActivity.class);
					startActivity(intent);
					WelcomePageActivity.this.finish();return;
				}
				int versionCode=AppUtil.getVersionCode();
				String versionName=AppUtil.getVersionName();
				try {
					JSONObject jo = new JSONObject(result);
					newVersionName=jo.getString("versionName");
					if(versionCode<jo.getInt("versionCode")){
						updateApp("应用版本已更新到"+newVersionName+",\n"+
									"您当前版本是"+versionName+",是否更新");
					}else{
						//timeUpdater.startTimer();
						//2  no new version
						Intent intent=new Intent(WelcomePageActivity.this,HomePageActivity.class);
						startActivity(intent);
						WelcomePageActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}.execute();

	}
	private void updateApp(String message){
		CustomizedDialog.Builder builder = new CustomizedDialog.Builder(this);
		builder.setTitle("版本更新提示");
		builder.setMessage(message);
		builder.setPositiveButton("马上更新",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface parent, int position) {
				//   timeUpdater.cancelTimer();
				DataService.THREAD_POOL.submit(new Downloader_v2());
			}
		});
		builder.setNegativeButton("日后再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//timeUpdater.startTimer();
				//3 no update 
				Intent intent=new Intent(WelcomePageActivity.this,HomePageActivity.class);
				startActivity(intent);
				WelcomePageActivity.this.finish();
			}
		});
		CustomizedDialog dialog=builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("lifeCycleInfo","@HomePageActivity  onActivityResult");
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d("lifeCycleInfo","@HomePageActivity  onNewIntent");
	}
	
	/**
	 * 
	 * inner class below
	 *
	 */
	
	public  class Downloader_v2 implements Runnable{
		public String newVersionName=WelcomePageActivity.this.newVersionName;
		public boolean active;
		public ProgressDialog pd;
		public Downloader_v2(){
			pd= new ProgressDialog(WelcomePageActivity.this,ProgressDialog.THEME_TRADITIONAL);
			pd.setTitle("正在下载");
			pd.setMessage("请稍后。。。");
			pd.setCanceledOnTouchOutside(false);
			pd.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setIndeterminate(false);
			pd.setButton(ProgressDialog.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//4   cancle update 
					active=false;Intent intent=new Intent(WelcomePageActivity.this,HomePageActivity.class);
					startActivity(intent);
					WelcomePageActivity.this.finish();
				}
			});
			pd.show();
		}
		public void run() {
			try {
				File file = AppUtil.getNetUtilInstance().appBreakPointDownload(this);
				if(active){
					Intent intent =new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(file), 
							 "application/vnd.android.package-archive");
					WelcomePageActivity.this.startActivity(intent);
					Log.d("netInfo","after download  @HomePageActivity");
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("netInfo","downloadError  @HomePageActivity");
			}finally{
				pd.dismiss();
			}
		}
		
	}
	
}
