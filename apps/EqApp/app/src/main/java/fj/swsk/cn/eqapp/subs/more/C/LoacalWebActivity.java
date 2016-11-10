package fj.swsk.cn.eqapp.subs.more.C;

import android.os.Bundle;
import android.webkit.WebView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;

/**
 * Created by apple on 16/9/30.
 */
public class LoacalWebActivity extends BaseTopbarActivity {

    WebView mWebView;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.layoutRes= R.layout.localweb_activity;
        super.onCreate(savedInstanceState);
        mWebView=(WebView)findViewById(R.id.webview);
        path=getIntent().getStringExtra("path");
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setTitleText(getIntent().getStringExtra("title"));
        mWebView.loadUrl(path);
    }
}
