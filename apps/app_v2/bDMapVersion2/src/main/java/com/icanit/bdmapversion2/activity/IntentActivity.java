package com.icanit.bdmapversion2.activity;

import com.icanit.bdmapversion2.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class IntentActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intent);
		Intent intent=getIntent();
		String title=intent.getStringExtra("title");
		((TextView)findViewById(R.id.textView1)).setText(title);
	}
}
