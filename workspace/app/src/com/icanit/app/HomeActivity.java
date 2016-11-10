package com.icanit.app;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.icanit.app.common.IConstants;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.exception.AppException;
import com.icanit.app.fragments.AbstractRadioBindFragment;
import com.icanit.app.fragments.Home_bottomtab01Fragment;
import com.icanit.app.fragments.Home_bottomtab02Fragment;
import com.icanit.app.fragments.Home_bottomtab03Fragment;
import com.icanit.app.fragments.Home_bottomtab04Fragment;
import com.icanit.app.service.DataService;

public class HomeActivity extends Activity implements
		Home_bottomtab01Fragment.ItemClickCallBack {
	private FrameLayout container;
	private RadioGroup radioGroup;
	private RadioButton rb0, rb1, rb2, rb3;
	private AbstractRadioBindFragment home_bottomtab01Fragment,
			home_bottomtab02Fragment, home_bottomtab03Fragment,
			home_bottomtab04Fragment;

	public AbstractRadioBindFragment getHome_bottomtab02Fragment() {
		return home_bottomtab02Fragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		try {
			init();
			DataService.THREAD_POOL.submit(new Runnable() {
				public void run() {
					home_bottomtab01Fragment = new Home_bottomtab01Fragment();
					home_bottomtab02Fragment = new Home_bottomtab02Fragment();
					home_bottomtab03Fragment = new Home_bottomtab03Fragment();
					home_bottomtab04Fragment = new Home_bottomtab04Fragment();
					initRadios();
					getFragmentManager().beginTransaction()
							.add(R.id.frameLayout1, home_bottomtab01Fragment)
							.commit();
				}
			});
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d("errorTag", intent.getAction() + "|@homeActivity onNewIntent "
				+ intent.getDataString());
		String action = intent.getAction();
		try {
			Bundle bundle = intent.getExtras();
			if (bundle != null && IConstants.AFTER_PAY.equals(action)) {
				Log.d("homeTag",
						new String(bundle.getByteArray("xml"), "utf-8"));
			} else if (IConstants.AFTER_COMMUNITY_CHANGE.equals(action)) {

			} else if (IConstants.AFTER_LOGIN.equals(action)) {

			} else if (IConstants.FORWARD_TO_PAY.equals(action)) {
				Log.d("errorTag",
						intent.getAction()
								+ " forwardToPay |@homeActivity onNewIntent "
								+ intent.getDataString());
				rb3.setChecked(true);
//				radioGroup.check(3);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void init() throws AppException {
		container = (FrameLayout) findViewById(R.id.frameLayout1);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	private void initRadios() {
		OnCheckedChangeListener listener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					switch (buttonView.getId()) {
					case R.id.radio0:
						if (!home_bottomtab01Fragment.isResumed())
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.frameLayout1,
											home_bottomtab01Fragment, null)
									.addToBackStack(null).commit();
						break;
					case R.id.radio1:
						if (!home_bottomtab02Fragment.isResumed())
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.frameLayout1,
											home_bottomtab02Fragment, null)
									.addToBackStack(null).commit();
						break;
					case R.id.radio2:
						if (!home_bottomtab03Fragment.isResumed())
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.frameLayout1,
											home_bottomtab03Fragment, null)
									.addToBackStack(null).commit();
						break;
					case R.id.radio3:
						if (!home_bottomtab04Fragment.isResumed())
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.frameLayout1,
											home_bottomtab04Fragment, null)
									.addToBackStack(null).commit();
						break;
					}
				}
			}
		};
		rb0 = (RadioButton) findViewById(R.id.radio0);
		home_bottomtab01Fragment.setTrigger(rb0);
		rb1 = (RadioButton) findViewById(R.id.radio1);
		home_bottomtab02Fragment.setTrigger(rb1);
		rb2 = (RadioButton) findViewById(R.id.radio2);
		home_bottomtab03Fragment.setTrigger(rb2);
		rb3 = (RadioButton) findViewById(R.id.radio3);
		home_bottomtab04Fragment.setTrigger(rb3);
		rb0.setOnCheckedChangeListener(listener);
		rb1.setOnCheckedChangeListener(listener);
		rb2.setOnCheckedChangeListener(listener);
		rb3.setOnCheckedChangeListener(listener);
	}

	@Override
	public void onItemClick(AppMerchant merchant) {
		Intent intent = new Intent(this, MerchandiseListActivity.class)
				.putExtra(IConstants.STORE_INFO, merchant);
		startActivity(intent);
	}
}
