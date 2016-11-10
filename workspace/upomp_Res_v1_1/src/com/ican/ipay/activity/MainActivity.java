package com.ican.ipay.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.ican.ipay.common.IConstants;
import com.ican.ipay.util.AppUtil;

public class MainActivity extends Activity implements Runnable {

	private String orderId, orderAmt, orderTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Uri data = intent.getData();

		String sign = data.getQueryParameter("sign");
		if (TextUtils.isEmpty(sign)) {
			finish();
			return;
		}
		new UpompPay().start_upomp_pay(MainActivity.this, sign);
		finish();

		/*
		 * orderId = data.getQueryParameter("orderId"); orderTime =
		 * data.getQueryParameter("orderTime"); orderAmt =
		 * data.getQueryParameter("orderAmt");
		 * System.out.println(orderId+"  @MainActivity"); if
		 * (TextUtils.isEmpty(orderId)) { finish(); return; }
		 * 
		 * IConstants.THREAD_POOL.submit(this);
		 */
	}

	@Override
	public void run() {
		final String launchPay = AppUtil.getOrderInfoNsign(orderId, orderAmt,
				orderTime);
		IConstants.MAIN_HANDLER.post(new Runnable() {
			public void run() {
				new UpompPay().start_upomp_pay(MainActivity.this, launchPay);
				finish();
			}
		});
	}
}
