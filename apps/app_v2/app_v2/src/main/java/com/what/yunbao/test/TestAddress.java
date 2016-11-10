package com.what.yunbao.test;

import com.what.yunbao.R;
import com.what.yunbao.address.AddressChoseActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class TestAddress extends Activity{
	TextView t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test3);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivityForResult(new Intent(TestAddress.this,AddressChoseActivity.class), 0);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			
			((TextView)findViewById(R.id.textView2)).setText(data.getStringExtra("name"));
			((TextView)findViewById(R.id.textView4)).setText(data.getStringExtra("phone"));
			((TextView)findViewById(R.id.textView6)).setText(data.getStringExtra("address"));
		
		}
	}
}
