package com.bshare.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bshare.utils.BSUtils;

/**
 * 
 * @author Chris.xue
 * 普通网页跳转式分享界面
 * 
 */
public class BShareBrowser extends Activity {
    private WebView browser;
    private Button backButton;
    private Button refreshButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(BSUtils.getResourseIdByName(this, "layout", "bshare_browser"));
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
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setSupportZoom(true);
        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        browser.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                super.onPageStarted(view, url, favicon);

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
                Toast.makeText(BShareBrowser.this, description, Toast.LENGTH_SHORT).show();
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });

        String url = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(url)) {
            browser.loadUrl(url);
            return;
        }

        refreshButton = (Button) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_button_refresh"));
        if (refreshButton != null) {
            refreshButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    browser.stopLoading();
                    browser.reload();
                }
            });
        }

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
