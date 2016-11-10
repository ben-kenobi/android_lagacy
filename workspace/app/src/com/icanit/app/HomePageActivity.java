package com.icanit.app;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app.common.FileCache;
import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.sqlite.AppSQLite;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.DeviceInfoUtil;
import com.icanit.app.util.SharedPreferencesUtil;

public class HomePageActivity extends Activity{
	public static int UPDATE_TIME=0;
	private TextView weather,date;
	private FrameLayout largeblock0,largeblock1,bottomblock;
	private SurfaceView surfaceView;
	private TimeUpdater timeUpdater;
	private DataService dataService;
	private String newVersionName;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		try {
			init();
			checkVersion();
		} catch (AppException e1) {
			e1.printStackTrace();
		}timeEtc();bindListeners();
		//checkAutoLoginIf();
		DeviceInfoUtil.checkNetwork(this);
		
		AppSQLite appsql=AppSQLite.getInstance(this);
		Cursor cursor=appsql.getMerchantInfoSet();
		String[] sary =new String[cursor.getCount()];
		for(int i=0;i<sary.length;i++){
			cursor.moveToPosition(i);
			sary[i]=cursor.getString(2);
		}
		cursor.close();
		AsyncTask<String, String, String> asyncTask=new AsyncTask<String, String, String>(){
			protected String doInBackground(String... params) {
				for(int i=0;i<=0;i++){
					publishProgress(params[i%params.length]);
				}
				return null;
			}
			protected void onProgressUpdate(String... values) {
				weather.setText(values[0]);
			}
		}.execute(sary);
		
//		Log.w("storageInfo","sdState="+Environment.getExternalStorageState()+" @HomepageActivity");
//		File sdCard=Environment.getExternalStorageDirectory();
//		Log.w("storageInfo","freeSpace="+sdCard.getFreeSpace()+",totalSpace="+
//		sdCard.getTotalSpace()+",useableSpace="+sdCard.getUsableSpace()+"  @HomePageActivity");
//		StatFs fs = new StatFs(sdCard.getPath());
//		  Log.w("storageInfo", "block大小="+ fs.getBlockSize()+",block数目="+ fs.getBlockCount()+",总大小="+fs.getBlockCount()*fs.getBlockSize()); 
//          Log.w("storageInfo", "可用的block数目：:"+ fs.getAvailableBlocks()+",剩余空间="+ fs.getAvailableBlocks()*fs.getBlockSize()); 
//          Log.w("storageInfo", "free的block数目：:"+ fs.getFreeBlocks()+",剩余空间="+ fs.getFreeBlocks()*fs.getBlockSize()); 
//		File root = Environment.getRootDirectory();
//		Log.w("storageInfo","rootSize="+root.getTotalSpace()+",rootAvailable="+root.getUsableSpace()+ "  @HomePageActivity");
	}
	
	
	protected void onResume() {
		super.onResume();
		timeUpdater.startTimer();
	}
	protected void onPause() {
		super.onPause();
		timeUpdater.cancelTimer();
	}
	protected void onDestroy() {
		Log.d("infoTag","@HomePageActivity  onDestroy");
		AppUtil.updateShoppingCartDB();
		AppUtil.deleteFileFromSDCard(newVersionName+IConstants.APP_NEWVERSION_FILE);
		FileCache.clear();
		super.onDestroy();
	}
	
	private void init() throws AppException{
		date=(TextView)findViewById(R.id.textView2);
		weather=(TextView)findViewById(R.id.textView3);
		largeblock0=(FrameLayout)findViewById(R.id.frameLayout1);
		largeblock1=(FrameLayout)findViewById(R.id.frameLayout2);
		bottomblock=(FrameLayout)findViewById(R.id.frameLayout3);
		surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
		timeUpdater=new TimeUpdater(surfaceView.getHolder());
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(this);
		
	}
	private void bindListeners(){
		largeblock0.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent ;
				if(AppUtil.getSharedPreferencesUtilInstance().hasReservedCommunityInfo()){
					intent = new Intent(HomePageActivity.this,HomeActivity.class);
				}else{
					intent=new Intent(HomePageActivity.this,ChangeCommunityActivity.class);
				}
				HomePageActivity.this.startActivity(intent);
			}
		});
		largeblock1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				Intent intent=new Intent(Intent.ACTION_VIEW).addCategory("android.intent.category.BROWSABLE")
//						.setData(Uri.parse("http://google.com:200")).setType("order/plain");
//				Log.w("intentInfo",intent.getDataString()+","+intent.getType()+" @HomePageActivity");
//				startActivity(intent);
			}
		});
		bottomblock.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppUtil.updateShoppingCartDB();
				AppUtil.deleteFileFromSDCard(newVersionName+IConstants.APP_NEWVERSION_FILE);
				FileCache.clear();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		});
	}
	
	private void timeEtc(){
		final SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		Calendar calendar=GregorianCalendar.getInstance();
		date.setText(new SimpleDateFormat("yyyy年MM月dd日").format(calendar.getTime())+
				" 星期"+digitTohebdomad(calendar.get(Calendar.DAY_OF_WEEK)));
		weather.setText("局部地区有雪");
	}
	
	
	
	private String digitTohebdomad(int digit){
		if(digit<1||digit>7) return "";
		switch(digit){
		case 1:return "日";
		case 2:return "一";
		case 3:return "二";
		case 4:return "三";
		case 5:return "四";
		case 6:return "五";
		case 7:return "六";
		}
		return "";
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
					timeUpdater.startTimer();	return;
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
						timeUpdater.startTimer();
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
				timeUpdater.cancelTimer();
				DataService.THREAD_POOL.submit(new Downloader());
			}
		});
		builder.setNegativeButton("日后再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				timeUpdater.startTimer();
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
	
	public  class Downloader implements Runnable{
		public String newVersionName=HomePageActivity.this.newVersionName;
		public boolean active;
		public ProgressDialog pd;
		public Downloader(){
			pd= new ProgressDialog(HomePageActivity.this,ProgressDialog.THEME_TRADITIONAL);
			pd.setTitle("正在下载");
			pd.setMessage("请稍后。。。");
			pd.setCanceledOnTouchOutside(false);
			pd.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setIndeterminate(false);
			pd.setButton(ProgressDialog.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					active=false;
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
					HomePageActivity.this.startActivity(intent);
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
	
	
	private static class TimeUpdater implements Runnable{
		SurfaceHolder surfaceHolder ;
		Paint paint;
		boolean active;
		private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss SSS");
		public TimeUpdater(SurfaceHolder holder){
			this.surfaceHolder=holder;
			initPaint();
		}
		private void  initPaint(){
			paint = new Paint();paint.setFakeBoldText(true);paint.setShadowLayer(4, -3, 4, Color.rgb(0x66, 0x66, 0x66));
			paint.setColor(Color.WHITE);paint.setAntiAlias(true);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(60);
		}
		public void startTimer(){
			if(!active){
				active=true;
				//TODO INVALID TIMER TEMPORARY
//				DataService.THREAD_POOL.submit(this);
				DataService.THREAD_POOL.submit(new TimeBuster());
			}
		}
		public void cancelTimer(){
			active=false;
		}
		public void run() {
			while(active){
				Canvas canvas = surfaceHolder.lockCanvas();
				if(canvas==null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;}
				canvas.drawColor(Color.rgb(0x00,0xbf,0xee));
				canvas.drawText(sdf.format(System.currentTimeMillis()),5,50, paint);
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
		
		public class TimeBuster implements Runnable{
			public void run() {
				while(active){
					Canvas canvas = surfaceHolder.lockCanvas();
					if(canvas==null) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;}
					canvas.drawColor(Color.rgb(0x00,0xbf,0xee));
					canvas.drawText(sdf.format(System.currentTimeMillis()),5,50, paint);
					surfaceHolder.unlockCanvasAndPost(canvas);
					active=false;
				}
			}
		}
		
	}

}
