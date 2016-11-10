package com.yf.accountmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.util.CommonUtils;

public class LoginActivity extends Activity implements OnClickListener {
	
	private int resId=R.layout.activity_login,
			accoungBg1=R.drawable.login0,
			filesystemBg1=R.drawable.login1,
			contactsBg1=R.drawable.login2;

	private ImageButton login;

	private EditText combination;

	private String name;
	
	private View contentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView =LayoutInflater.from(this).inflate(resId,null,false);
		setContentView(contentView);
		name = getIntent().getStringExtra(IConstants.NAME);
		if(IConstants.ACCOUNT.equals(name))
			contentView.setBackgroundResource(accoungBg1);
		else if(IConstants.FILESYSTEM.equals(name))
			contentView.setBackgroundResource(filesystemBg1);
		else if(IConstants.CONTACTS.equals(name))
			contentView.setBackgroundResource(contactsBg1);
		init();
		bindListeners();
	}

	private void init() {
		login = (ImageButton) findViewById(R.id.imageButton1);
		combination = (EditText) findViewById(R.id.editText1);
	}

	private void bindListeners() {
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == login) {
			String pwd = combination.getText().toString();
			if (TextUtils.isEmpty(pwd) || !CommonService.login(pwd)){
				finish();
				return;
			}
			Intent intent = CommonService.getIntentByAccessName(name,this);
			if (intent != null) {
				startActivity(intent);
				CommonUtils.setAccessKey(pwd);
			}
			finish();

		}
	}
}
