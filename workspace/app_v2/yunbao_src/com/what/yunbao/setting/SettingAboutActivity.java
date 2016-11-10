package com.what.yunbao.setting;

import com.what.yunbao.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingAboutActivity extends Activity{
	private ImageButton about_back_btn = null;

    private TextView mSettingAboutVersionTextView;
    private WebView mSettingAboutLinkWebView;
    private RelativeLayout about_tel_rl;
    private RelativeLayout webView_rl;
    
    private String phone = "10086";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_about);

        about_back_btn  = (ImageButton) findViewById(R.id.ib_setting_about_back);
        about_back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSettingAboutVersionTextView = (TextView) findViewById(R.id.tv_setting_about_version);
        PackageInfo pinfo;
        try {
            pinfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            mSettingAboutVersionTextView.setText("v"+pinfo.versionName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //webView动态生成 便于回收  使用application
        mSettingAboutLinkWebView = new WebView(this);
        webView_rl = (RelativeLayout) findViewById(R.id.rl_wv_setting_about_link);       
//        mSettingAboutLinkWebView.loadUrl("file:///android_asset/setting_about_link.html");
        webView_rl.addView(mSettingAboutLinkWebView);
        
        mSettingAboutLinkWebView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl("http://www.google.hk");
        		return true;
        	}
        });
        
        
        about_tel_rl = (RelativeLayout) findViewById(R.id.rl_setting_about_tel);
        about_tel_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone)); 
	        	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	        	Toast.makeText(SettingAboutActivity.this, "工作时间：每天9:00-20:00", 3000).show();
	        	startActivity(intent);
				
			}
		});        
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	webView_rl.removeView(mSettingAboutLinkWebView);
    	mSettingAboutLinkWebView.setFocusable(true);
    	mSettingAboutLinkWebView.removeAllViews();
    	mSettingAboutLinkWebView.clearHistory();
    	mSettingAboutLinkWebView.destroy();
    }
}
