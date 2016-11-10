package com.icanit.app_v2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.util.AppUtil;
import com.what.yunbao.R;

public class AccountActivity extends Activity {
	private TextView recharge_tv;
	private Button modifyPassword, logout;
	private ImageView backButton;
	private TextView balance, consumed, integral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_main);
		init();
		bindListeners();
		
	}
	private void completeInfo(){
			AppUser user = AppUtil.getLoginUser();
		if(user!=null){
			balance.setText("гд "+String.format("%.2f", 0.0));
			consumed.setText("гд "+String.format("%.2f", 0.0));
			integral.setText("гд "+String.format("%.0f", 0.0));
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		completeInfo();
	}
	private void init() {
		recharge_tv = (TextView) findViewById(R.id.tv_account_recharge);
		modifyPassword = (Button) findViewById(R.id.button1);
		logout = (Button) findViewById(R.id.button2);
		backButton = (ImageButton) findViewById(R.id.ib_account_back);
		balance = (TextView) findViewById(R.id.textView1);
		consumed = (TextView) findViewById(R.id.textView2);
		integral = (TextView) findViewById(R.id.textView3);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		recharge_tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AccountActivity.this,
						AccountRechargeActivity.class));
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6,
						R.anim.anim_down_toright6);
			}
		});
		logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppUtil.logout();
				startActivity(new Intent(AccountActivity.this,
						MainActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
		modifyPassword.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(AccountActivity.this,
						ModifyPasswordActivity.class));
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6,
					R.anim.anim_down_toright6);
			return true;
		default:
			return super.dispatchKeyEvent(event);
		}
	}
}
