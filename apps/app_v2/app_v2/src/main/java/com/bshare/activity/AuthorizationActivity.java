package com.bshare.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bshare.core.PlatformType;
import com.bshare.core.TokenInfo;
import com.bshare.core.UserInfo;
import com.bshare.platform.Platform;
import com.bshare.platform.PlatformFactory;
import com.bshare.utils.BSUtils;

/**
 * 
 * @author Chris.xue
 * 授权认证界面
 */
public class AuthorizationActivity extends Activity {
    private WebView browser;
    private Button backButton;
    private Button refreshButton;
    private ProgressBar progressBar;
    private Platform platform;
    private int position;

    @TargetApi(3)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(BSUtils.getResourseIdByName(this, "layout", "bshare_browser"));
        position = getIntent().getIntExtra("position", -1);
        backButton = (Button) findViewById(BSUtils.getResourseIdByName(this, "id", "browser_back_button"));
        progressBar = (ProgressBar) findViewById(BSUtils.getResourseIdByName(this, "id", "browser_progress_bar"));
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                browser.stopLoading();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        browser = (WebView) findViewById(BSUtils.getResourseIdByName(this, "id", "bshare_web"));
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(false);
        browser.getSettings().setSupportZoom(true);
        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        platform = PlatformFactory.getPlatform(this, (PlatformType) getIntent().getParcelableExtra("platform"));
        browser.setWebViewClient(new WebViewClient() {

            @SuppressLint("NewApi")
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                super.onPageStarted(view, url, favicon);

                if (!TextUtils.isEmpty(url) && url.startsWith(platform.getCallbackUrl())) {
                    view.stopLoading();
                    final Uri uri = Uri.parse(url);
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {

                            Intent resultIntent = new Intent();
                            Bundle extras = new Bundle();
                            TokenInfo tokenInfo = platform.retrieveTokenInfo(getApplication(), uri);
                            extras.putParcelable("tokenInfo", tokenInfo);
                            if (tokenInfo != null) {
                                UserInfo userInfo = platform.retrieveUserInfo(getApplication(), tokenInfo);
                                extras.putParcelable("userInfo", userInfo);
                                setResult(Activity.RESULT_OK, resultIntent);
                            } else {
                                extras.putParcelable("userInfo", null);
                                setResult(Activity.RESULT_CANCELED, resultIntent);
                            }
                            resultIntent.putExtras(extras);
                            resultIntent.putExtra("position", position);
                            finish();
                        }
                    });
                    progressBar.setProgress((int) (Math.random() * 100));
                    thread.start();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                Toast.makeText(AuthorizationActivity.this, description, Toast.LENGTH_SHORT).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
        String url = getIntent().getStringExtra("url");
        System.out.println("url=" + url);

        if (!TextUtils.isEmpty(url)) {
            browser.loadUrl(url);
            return;
        }

        refreshButton = (Button) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_button_refresh"));
        refreshButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                browser.stopLoading();
                browser.reload();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (browser != null) {
                browser.stopLoading();
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
