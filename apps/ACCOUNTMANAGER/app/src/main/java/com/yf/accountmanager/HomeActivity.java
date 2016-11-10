package com.yf.accountmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.util.CommonUtils;

public class HomeActivity extends Activity implements OnClickListener {
	private int resId = R.layout.activity_home, bg1 = R.drawable.home,
			bg2 = R.drawable.home2,
			bg3=R.drawable.access;
	private int[] bgary=new int[]{bg1,bg2,bg3};
	private ImageButton account, filesystem, contacts, exit;

	private View contentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = LayoutInflater.from(this)
				.inflate(resId, null, false);
		setContentView(contentView);
		init();
		bindListeners();
	}

	private void init() {
		account = (ImageButton) findViewById(R.id.imageButton1);
		filesystem = (ImageButton) findViewById(R.id.imageButton2);
		contacts = (ImageButton) findViewById(R.id.imageButton3);
		exit = (ImageButton) findViewById(R.id.imageButton4);
	}

	private void bindListeners() {
		account.setOnClickListener(this);
		filesystem.setOnClickListener(this);
		contacts.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	private void forwardToLogin(String name) {
		Intent intent = new Intent(this, LoginActivity.class).putExtra(
				IConstants.NAME, name);
		startActivity(intent);
	}

	private void forwardTo(String name) {
		if(CommonService.isAccessKeyEnable(name)){
			forwardToLogin(name);
		}else{
			Intent intent=CommonService.getIntentByAccessName(name,this);
			if(intent!=null)
			startActivity(intent);
		}
	}

	
	@Override
	public void onClick(View v) {
		if (v == account) {
			forwardTo(IConstants.ACCOUNT);
		} else if (v == filesystem) {
			forwardTo(IConstants.FILESYSTEM);
		} else if (v == contacts) {
			forwardTo(IConstants.CONTACTS);
		} else if (v == exit) {
//			Process.killProcess(Process.myPid());
			finish();
		}
	}
	@Override
	protected void onResume() {
		contentView.setBackgroundResource(bgary[(int)(Math.random()*10)%3]);
		super.onResume();
		CommonUtils.setAccessKey(null);
	}
	@Override
	protected void onPause() {
		super.onPause();
		CommonUtils.setAccessKey(null);
	}
}
