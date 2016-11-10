package com.icanit.app_v2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.ui.InnerListView;

public class TestActivity extends Activity{
	InnerListView lv;
	ScrollView sv;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text02);
		init();initlv();
	}
	private void init(){
		lv=(InnerListView)findViewById(R.id.listView1);
		sv=(ScrollView)findViewById(R.id.scrollView1);
		lv.setParentScrollView(sv);
	}
	private void initlv(){
	}
}
