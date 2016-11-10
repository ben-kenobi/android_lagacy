package com.what.yunbao.setting;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.common.IConstants;
import com.umeng.fb.FeedbackAgent;
import com.what.weibo.WeiboMainActivity;
import com.what.yunbao.R;
import com.what.yunbao.update.AppUpgradeService;
import com.what.yunbao.update.MyAlertDialog;
import com.what.yunbao.util.CommonUtil;
import com.what.yunbao.util.DataCleanManager;

public class SettingTabActivity extends Activity implements OnItemClickListener{
	private static final String TAG = "SettingTabActivity";
	private ImageButton setting_back_save_bt;
	private Map<Integer,Boolean> mSelectedIndex;
	
    private String[] mSettingItems = {"清除缓存",
                                       "2G/3G下自动接收图片",
                                       "展示我的动态",
                                       "检查更新",
                                       "",
                                       "意见反馈",
                                       "新手帮助",
                                       "隐私政策",
                                       "关于我们",
                                       "分享软件"};
    private String[] mSettingItemMethods = {"clearCache",
                                            "receivePicture",
                                            "displayDynamic ",
                                            "checkNewVersion",
                                            "",
                                            "feedBackSuggestion",
                                            "help",
                                            "privacy",
                                            "about",
                                            "shareApp"};
    private HashMap<String, String> mSettingItemMethodMap = new HashMap<String, String>();
    
    private List<List<Map<String,String>>> listDatas = new ArrayList<List<Map<String,String>>>();
    
   
    private CornerListView cornerListView;
    private SettingAdapter adapter_1;
    
    private Toast mToast = null;
    private int count =0;
    
    private String cacheSize;
    private int externalMemberVariable = 0;//外部缓存size
    private int internalMemberVariable = 0;
    
    private PackageManager pm;
    
//    private Handler handler = new Handler(){
//    	@Override
//    	public void handleMessage(Message msg) {
//    		
//    		
//    		super.handleMessage(msg);
//    		Log.e(TAG, "cacheSize-2: " + cacheSize);	
//			adapter_1.setSelectedIndex(mSelectedIndex);
//			adapter_1.setCacheSize(cacheSize);
//			adapter_1.notifyDataSetChanged();
//    	}
//    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	Log.e("oncreate------------", "msg");
    	setContentView(R.layout.setting);
    	Log.e(TAG,"1---------------------"+System.currentTimeMillis());
    	setDatas();
    	Log.e(TAG,"2---------------------"+System.currentTimeMillis());    	
    	new GetPreferences().execute();
    	Log.e(TAG,"3---------------------"+System.currentTimeMillis());
    	new GetPackageStats().execute();
    	Log.e(TAG,"4---------------------"+System.currentTimeMillis());
    	setUpLayout();
    	mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
    	Log.e(TAG,"5---------------------"+System.currentTimeMillis());
    	 	
    }    

    public void setUpLayout() {  
 	
    	setting_back_save_bt = (ImageButton) findViewById(R.id.ib_setting_back);
    	setting_back_save_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SettingTabActivity.this.finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
    	findViewById(R.id.tv_setting_account_manager).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SettingTabActivity.this,WeiboMainActivity.class));	
			}
		});
    	 
    	LinearLayout cornerContainer = (LinearLayout) findViewById(R.id.setting);
        for (int i = 0; i < mSettingItems.length; i++) {
            mSettingItemMethodMap.put(mSettingItems[i], mSettingItemMethods[i]);
        }

        int size = listDatas.size();

        LayoutParams lp;
        SettingAdapter adapter;
        for (int i = 0; i < size; i++) {//size = 2
            cornerListView = new CornerListView(this);
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            // 左上右下
//            if (i == 0 && i == (size - 1)) {
//                lp.setMargins(8, 8, 8, 8);                
//            } else if (i == 0) {
//                lp.setMargins(8, 8, 8, 4);
//            }else if (i == (size - 1)) {
//                lp.setMargins(8, 4, 8, 8);
//            } else {
                lp.setMargins(8, 4, 8, 4);
//            }
            cornerListView.setLayoutParams(lp);
            cornerListView.setDivider(getResources().getDrawable(R.drawable.app_divider_h_gray));
            cornerContainer.addView(cornerListView);      	        
            if(i == 1){
                adapter = new SettingAdapter(this, listDatas.get(i));
                cornerListView.setAdapter(adapter);
            }else {
            	adapter_1 = new SettingAdapter(this, listDatas.get(i));
                cornerListView.setAdapter(adapter_1);
            }

            cornerListView.setOnItemClickListener(this);
            int height = listDatas.get(i).size() * (int) getResources().getDimension(R.dimen.setting_item_height);
            height += 1;
            cornerListView.getLayoutParams().height = height;    
           
        }
    }
    

    public void setDatas() {
        listDatas.clear();
        List<Map<String,String>> listData = new ArrayList<Map<String,String>>();

        Map<String,String> map;

        for(int i = 0; i < mSettingItems.length; i++) {
            if ("".equals(mSettingItems[i])) {
                listDatas.add(listData);//空值的
                listData = new ArrayList<Map<String,String>>();
            } else {
                map = new HashMap<String, String>();
                map.put("text", mSettingItems[i]);
                listData.add(map);
            }
        }

        listDatas.add(listData);//有值的
    }   

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.tv_setting_list_item_text);
        String key = textView.getText().toString();
        Class<? extends SettingTabActivity> clazz = this.getClass();
        try {
            Method method = clazz.getMethod(mSettingItemMethodMap.get(key));
            method.invoke(SettingTabActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
		
	/**
	 * 获取缓存信息
	 * @param pkg
	 */
//	public void getpkginfo(String pkg){	
//		pm = getApplicationContext().getPackageManager();
//		try {			
//			
//		    Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
//			getPackageSizeInfo.invoke(pm, pkg,new PkgSizeObserver());//这句话导致内存泄漏
//		} catch (Exception e) {
//		}
//		
//	}
//	
//	private class PkgSizeObserver extends IPackageStatsObserver.Stub {
//		@Override
//	    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
////	         Message msg = mHandler.obtainMessage(1);
////	         Bundle data = new Bundle();
////	         data.putParcelable("what", pStats);
////	         msg.setData(data);
////	         mHandler.sendMessage(msg);
//			if (pStats!=null) {
//				cacheSize = CommonUtil.formatFileSize(pStats.cacheSize + externalMemberVariable);//缓存大小
////				handler.sendEmptyMessage(0);
//				Log.e(TAG, "cacheSize-1: " + cacheSize);
//			}	         
//	     }
//	}

    public void clearCache() {
    	DataCleanManager.cleanInternalCache(this);//(/data/data/com.xxx.xxx/cache) 
        DataCleanManager.cleanExternalCache(this);//(/mnt/sdcard/android/data/com.xxx.xxx/cache)
        adapter_1.setCacheSize("0B");
        adapter_1.notifyDataSetChanged();
    	CommonUtil.showToast("缓存已清空", mToast);
    }
    


    public void checkNewVersion(){
       	//检测网络状态
	    if (!CommonUtil.isNetworkAvailable(SettingTabActivity.this)) {
	        CommonUtil.showToast("对不起，您未连接网络。", mToast);
	        return;
	    }
    	new AsyncTask<String, Integer, String>() {
			@Override
			protected String doInBackground(String... params) {
				PackageInfo packageInfo;
				try  { 
					packageInfo = SettingTabActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);				
					int localVersion = packageInfo.versionCode;
					//发送请求获取版本 
					int serverVersion=2;					
					if(localVersion<serverVersion){
						return "update";
					}					
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}				
				return "nonUpdate";
			}
			
			@Override
			protected void onPostExecute(String result) {
				if("update".equals(result)){
					final MyAlertDialog dialog = new MyAlertDialog(SettingTabActivity.this);
					dialog.setTitle("发现新版本，建议立即更新使用。");
					dialog.setMessage("1.增加 XXX功能\n2.提升用户体验\n3.修复XXXbug");
					dialog.setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							dialog.dismiss();							
						}
					});
					dialog.setPositiveButton("更新", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent updateIntent=new Intent(SettingTabActivity.this,AppUpgradeService.class);
							updateIntent.putExtra("downloadUrl", IConstants.APP_DOWNLOAD_PATH);
							//TODO
							updateIntent.putExtra("app_name", getResources().getString(R.string.app_name));
							startService(updateIntent);	
							dialog.dismiss();
						}
					});

				}else{
					CommonUtil.showToast("您的版本是最新版本,不需要更新。", mToast);
				}
			}
		}.execute();
    }

    public void feedBackSuggestion(){
    	CommonUtil.showToast("建议", mToast);
//    	startActivity(new Intent(this,
//    			SettingSuggestionActivity.class));

		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		agent.startFeedbackActivity();
   	
    }
    public void help(){
    	Intent intent = new Intent(this,SettingGuideActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	startActivity(intent);
    }
    
    public void privacy(){
    	Intent intent = new Intent(this,SettingPrivacyWebViewActivity.class);
    	startActivity(intent);
    	
    }
    public void shareApp() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.setting_share_app_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.setting_share_app_body) + "www.google.hk");
        startActivity(Intent.createChooser(intent, getString(R.string.setting_share_app_title)));
    }
    
    public void about() {
        Intent intent = new Intent(this, SettingAboutActivity.class);
        startActivity(intent);
    }

    private void getPreference(){
    	mSelectedIndex = new HashMap<Integer,Boolean>();
    	boolean status = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("receivePicture", true);
        mSelectedIndex.put(1, status);
        status = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("displayDynamic", false);
        mSelectedIndex.put(2, status); 
    }
    
    private void getExternalCache(){
    	if(CommonUtil.hasSDCard()){
//	    	getExternalCacheDir()
	    	File file = getExternalCacheDir();
		        if(file.exists()){		        
			        if(file.isDirectory()){
			        	Log.e(TAG, "externale files : "+Arrays.toString(file.list()));
			        	Log.e(TAG, "文件占用空间大小"+file.getTotalSpace()+"和文件大小不一样");
			        	int externalLocalVariables = 0;
			        	for(int i = 0;i < file.listFiles().length;i++){
			        		externalLocalVariables += file.listFiles()[i].length();
			        	}
			        	externalMemberVariable = externalLocalVariables;
			        	Log.e(TAG, "externale files size : "+externalLocalVariables);
			        }
	        }
    	}
    }
    private void getInternalCache(){    	
    	File file = new File(String.valueOf(getCacheDir()));
	        if(file.exists()){		        
		        if(file.isDirectory()){
		        	int internalLocalVariables = 0;
		        	for(int i = 0;i < file.listFiles().length;i++){
		        		internalLocalVariables += file.listFiles()[i].length();
		        	}
		        	internalMemberVariable = internalLocalVariables;
		        	Log.e(TAG, "externale files size : "+internalLocalVariables);
		        }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//      setDatas();
//    	setUpLayout();        
    	Log.e(TAG,"onresume------------");  	 
    }
    
    @Override
    protected void onPause() {   	
    	super.onPause();
    	Log.e(TAG,"onpause------------");
    	mSelectedIndex = adapter_1.getSelectedIndex();
    	if(mSelectedIndex!=null){
    		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        	sp.edit().putBoolean("receivePicture", mSelectedIndex.get(1)).commit();
        	sp.edit().putBoolean("displayDynamic", mSelectedIndex.get(2)).commit();
    	}
    	
    }   
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//清除引用
    	if(cornerListView != null){
    		cornerListView = null;
    	}
    	
    }
    
    private class GetPackageStats extends AsyncTask<Void, String, String>{

		@Override
		protected String doInBackground(Void... params) {

			getExternalCache();//获取外部的缓存  不过外部真的不好管理 软件卸载了还存在手机中
			getInternalCache();
//			getpkginfo("com.what.yunbao");//获取内部的缓存 用其他封装好的方法 获取速度快 但是这方法内存
			cacheSize = CommonUtil.formatFileSize(externalMemberVariable+internalMemberVariable);
			return cacheSize;
		}
		
		@Override
		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
			
			Log.e(TAG, "cacheSize-2: " + cacheSize);	
			adapter_1.setCacheSize(cacheSize);
			adapter_1.notifyDataSetChanged();
		}  	
    }
    private class GetPreferences extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			getPreference();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);	
			adapter_1.setSelectedIndex(mSelectedIndex);
			adapter_1.setCacheSize("计算中..");
			adapter_1.notifyDataSetChanged();
		}  	
    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
    		finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return true;
    	}else{
    		return super.dispatchKeyEvent(event);
    	}
	}
    
}
