package com.what.yunbao.setting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class SettingPrivacyWebViewActivity extends Activity {
	Button bt;
	ProgressWebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new ProgressWebView(this,null);
		setContentView(webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://www.google.cn/intl/zh-CN/policies/privacy/");
		
//		webView.addJavascriptInterface(new Handler(), "handler");
//		webView.setWebViewClient(new WebViewClient() {
//			@Override
//			public void onPageFinished(WebView view, String url) {
//				Toast.makeText(SettingPrivacyWebViewActivity.this, "网页加载完成", 0).show();
//				view.loadUrl("javascript:window.handler.show(document.body.innerHTML);");
//				super.onPageFinished(view, url);
//			} 
//		});
		

//        webview.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                if (url != null && url.startsWith("http://")){}
////                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            }
//        });

	}

//	class Handler {
//		public void show(String data) {
//			Toast.makeText(SettingPrivacyWebViewActivity.this, "执行了handler.show方法", 0).show();
//			new AlertDialog.Builder(SettingPrivacyWebViewActivity.this).setMessage(data).create().show();
//		}
//	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.setFocusable(true);
		webView.removeAllViews();
		webView.clearHistory();
		webView.destroy();
	}
}