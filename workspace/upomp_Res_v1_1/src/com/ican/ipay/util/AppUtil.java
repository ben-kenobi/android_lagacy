package com.ican.ipay.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.ican.ipay.activity.MapApplication;
import com.ican.ipay.common.UriConstants;

public final class AppUtil {
	public static MapApplication appContext;
	public static Toast globalToast;

	

	public static void toast(String message) {
		if (globalToast == null) {
			globalToast = Toast.makeText(appContext, null, Toast.LENGTH_SHORT);
		}
		globalToast.setText(message);
		globalToast.show();
	}

	
//	public static String getOrderInfoNsign(String orderId) {
//		String response = null;
//		try {
//			List<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("orderId", orderId));
//			response = AppUtil.getNetUtilInstance().sendMessageWithHttpPost(
//					UriConstants.GET_ORDERINFO_N_SIGN, params);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return response;
//	}
//TODO
	public static String getOrderInfoNsign(String orderId,String orderAmt,String orderTime) {
		String response = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("merchantOrderId", orderId));
			params.add(new BasicNameValuePair("merchantOrderAmt", orderAmt));
			params.add(new BasicNameValuePair("merchantOrderTime", orderTime));
			response = AppUtil.getNetUtilInstance().sendMessageWithHttpPost(
					UriConstants.GET_ORDERINFO_N_SIGN, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	public static NetUtil getNetUtilInstance() {
		return NetUtil.getInstance();
	}
	
	public static boolean isPhoneNum(String phone) {
		if (phone == null)
			return false;
		return phone.matches("^[1]([3][0-9]{1}|59|56|58|88|89)[0-9]{8}$");
	}

	public static boolean isDecimal(String decimal, int scale) {
		if (TextUtils.isEmpty(decimal))
			return false;
		return decimal.matches("^[0-9]*\\.?[0-9]{0," + scale + "}$")
				&& Double.parseDouble(decimal) > 0;
	}

	public static boolean isPassword(String password) {
		if (password == null)
			return false;
		return password.matches("^\\w{6,18}$");
	}

	public static boolean isNumber(String number) {
		if (number == null)
			return false;
		return number.matches("^\\d+$");
	}

	public static boolean isVeriCode(String veriCode) {
		if (veriCode == null)
			return false;
		return veriCode.length() == 3;
	}

	public static int getVersionCode() {
		try {
			return appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	

	public static void adapterNotifyInHandler(Handler handler,
			final BaseAdapter adapter) {
		handler.post(new Runnable() {
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	public static void setOnClickListenerForCheckedTextView(
			final CheckedTextView ctv) {
		ctv.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (ctv.isChecked())
					ctv.setChecked(false);
				else
					ctv.setChecked(true);
			}
		});
	}

	public static void setOnClickListenerForSelectionView(final View v) {
		v.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (v.isSelected())
					v.setSelected(false);
				else
					v.setSelected(true);
			}
		});
	}

	

	
	public static int calculateListViewHeight(ListView lv, int itemHeight,
			int itemCount) {
		int verticalPaddingHeight = lv.getPaddingBottom() + lv.getPaddingTop();
		if (itemCount == 0)
			return verticalPaddingHeight;
		return itemHeight * itemCount + lv.getDividerHeight() * (itemCount - 1)
				+ verticalPaddingHeight;
	}

	public static int getMeasuredHeight(View view) {
		view.measure(0, 0);
		return view.getMeasuredHeight();
	}

	

	public static String getOrderStatusDescByOrderStatus(int status) {
		return status == 0 ? "未支付" : status == 1 ? "已支付" : status == 2 ? "发货中"
				: status == 3 ? "确认完成" : status == 4 ? "撤销中"
						: status == 5 ? "撤销完成" : status == 6 ? "退货中"
								: status == 7 ? "退货完成" : status == 8 ? "货品未收到"
										: status == 9 ? "问题订单" : "";
	}

	
	public static String formatPhoneNum(String phone) {
		if (phone.length() != 11)
			return phone;
		char[] chary = phone.toCharArray();
		for (int i = 0; i < 4; i++) {
			chary[i + 3] = '*';
		}

		return new String(chary);
	}



	public static byte[] getAssetResAsBytes(String name, Context context)
			throws Exception {
		InputStream is = context.getAssets().open(name);
		byte[] bytes = new byte[0];
		byte[] buf = new byte[1024];
		int count = 0, olength = 0;
		while ((count = is.read(buf)) != -1) {
			olength = bytes.length;
			bytes = Arrays.copyOf(bytes, olength + count);
			System.arraycopy(buf, 0, bytes, olength, count);
		}
		is.close();
		return bytes;
	}



	public static void deleteFileRecursively(File file, boolean retainDir) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFileRecursively(files[i], false);
			}
		}
		if (file.isFile() || !retainDir)
			file.delete();
	}

	public static long getDirBytes(File file) {
		if (!file.exists())
			return 0;
		long size = 0;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				size += getDirBytes(files[i]);
			}
		} else {
			size = file.length();
		}
		return size;
	}

	public static void intentToCall(String phoneNum, Context context) {
		Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum)); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	context.startActivity(intent);
	}

	public static void intentToDial(String phoneNum, Context context) {
		Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNum)); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	context.startActivity(intent);
	}
	
	public static String formatMoney(Double money){
		return String.format("%.2f", money);
	}

}
