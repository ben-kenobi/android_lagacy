package com.icanit.app_v2.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.fragment.PayHomeFragment;
import com.icanit.app_v2.fragment.PayResultFragment;
import com.icanit.app_v2.ui.CustomizedDialog;
import com.icanit.app_v2.util.AppUtil;

public class PayIndexActivity extends FragmentActivity implements
		OnClickListener {

	private int resId = R.layout.activity_pay_index;

	private int containerId = R.id.frameLayout1;

	private Button backButton;

	private ImageView logo;

	private FrameLayout container;

	private Fragment homeFragment, resultFragment;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		init();
		bindListeners();
		commitFragment();
	}
	
	public void informPayResult(AppOrder order){
		if(resultFragment==null)
			resultFragment=new PayResultFragment();
		AppUtil.updateUser();
		getIntent().putExtra(IConstants.ORDER_INFO, order);
		getSupportFragmentManager().beginTransaction().replace(containerId, resultFragment).commitAllowingStateLoss();
		homeFragment=null;
		setResult(2002,getIntent());
	}

	private void commitFragment() {
		if (homeFragment == null)
			homeFragment = new PayHomeFragment();
		getSupportFragmentManager().beginTransaction()
				.add(containerId, homeFragment).commitAllowingStateLoss();
	}

	private void init() {
		backButton = (Button) findViewById(R.id.button1);
		backButton.setText("返回");
		logo = (ImageView) findViewById(R.id.imageView1);
		container = (FrameLayout) findViewById(containerId);
	}

	private void bindListeners() {
		backButton.setOnClickListener(this);
	}

	private void createConfirmDialogNshow() {
		if (resultFragment!=null) {
			PayIndexActivity.this.finish();
		} else {
			CustomizedDialog dialog = CustomizedDialog.initDialog("退出交易",
					"确定取消本次支付？", null, 0, this);
			dialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							setResult(2002);
							PayIndexActivity.this.finish();
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			createConfirmDialogNshow();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.button1) {
			createConfirmDialogNshow();
		}

	}
}
