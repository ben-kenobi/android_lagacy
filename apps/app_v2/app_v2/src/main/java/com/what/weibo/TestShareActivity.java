package com.what.weibo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TestShareActivity extends Activity{
//	private WeiBoShare share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
//		share = new WeiBoShare();
 		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiBoShare.shareToSina(TestShareActivity.this,"","");
			}
		});
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WeiBoShare.shareToTecent(TestShareActivity.this,"","","");
			}
		});
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		share = null;
	}
}
